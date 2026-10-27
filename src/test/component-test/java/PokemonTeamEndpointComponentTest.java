import com.rest.pokemon.PokemonApplication;
import com.rest.pokemon.config.CacheConfig;
import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.api.dto.PokemonDetailsResponseDto;
import com.rest.pokemon.api.dto.PokemonTeamResponseDto;
import com.rest.pokemon.api.dto.StatisticsResponseDto;
import com.rest.pokemon.external.pokeapi.PokeApiClient;
import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticsDto;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.exceptionhandler.ProblemDto;
import com.rest.pokemon.repository.PokemonTeamDao;
import com.rest.pokemon.repository.exception.PokemonTeamNameAlreadyExistsException;
import com.rest.pokemon.repository.exception.PokemonTeamNotFoundException;
import configuration.AbstractComponentTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {PokemonApplication.class, AbstractComponentTestConfiguration.class}
)
@ActiveProfiles("component-test")
public class PokemonTeamEndpointComponentTest {

    private final PokemonTeam testPokemonTeam = new PokemonTeam(1, "Ash's team", List.of(
            createPokemon("Pikachu", 25, 35),
            createPokemon("Charmander", 4, 39)
    ));

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PokemonTeamDao pokemonTeamDao;

    @Autowired
    private PokeApiClient pokeApiClient;

    @Autowired
    private Random random;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        reset(random, pokeApiClient, pokemonTeamDao);
        cacheManager.getCache(CacheConfig.POKEMON_LIST_CACHE).clear();
        cacheManager.getCache(CacheConfig.POKEMON_DETAILS_CACHE).clear();
    }

    @Test
    void shouldReturnMyPokemonTeam() {
        //given
        var expectedResponse = new PokemonTeamResponseDto(1, "Ash's team", List.of(
                new PokemonDetailsResponseDto(25, "Pikachu", new StatisticsResponseDto(35, 35, 35, 35)),
                new PokemonDetailsResponseDto(4, "Charmander", new StatisticsResponseDto(39, 39, 39, 39))
        ));
        when(pokemonTeamDao.getById(1)).thenReturn(testPokemonTeam);

        //when
        var response = restTemplate.exchange("/v1/pokemon/team/1", HttpMethod.GET, null, PokemonTeamResponseDto.class);

        //then
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenTeamDoesNotExist() {
        //given
        var missingTeamId = 99L;
        when(pokemonTeamDao.getById(missingTeamId))
                .thenThrow(new PokemonTeamNotFoundException(String.valueOf(missingTeamId)));

        //when
        var response = restTemplate.exchange(
                "/v1/pokemon/team/" + missingTeamId, HttpMethod.GET, null, ProblemDto.class);

        //then
        assertEquals(404, response.getStatusCode().value());
        assertEquals(404, response.getBody().status());
        assertEquals("Pokemon team not found: 99", response.getBody().detail());
        assertEquals("/v1/pokemon/team/99", response.getBody().instance());
    }

    @Test
    void shouldRejectBlankTeamName() {
        //given
        var blankTeamName = "   ";

        //when
        var response = restTemplate.exchange(
                "/v1/pokemon/team?name=" + blankTeamName, HttpMethod.POST, null, ProblemDto.class);

        //then
        assertEquals(400, response.getStatusCode().value());
        assertEquals(400, response.getBody().status());
        assertEquals("/v1/pokemon/team", response.getBody().instance());
    }

    @Test
    void shouldRejectTeamNameLongerThanTwentyCharacters() {
        //given
        var tooLongTeamName = "A".repeat(21);

        //when
        var response = restTemplate.exchange(
                "/v1/pokemon/team?name=" + tooLongTeamName, HttpMethod.POST, null, ProblemDto.class);

        //then
        assertEquals(400, response.getStatusCode().value());
        assertEquals(400, response.getBody().status());
        assertEquals("/v1/pokemon/team", response.getBody().instance());
    }

    @Test
    void shouldReturnAllPokemonTeams() {
        //given
        var firstTeam = testPokemonTeam;
        var secondTeam = new PokemonTeam(2, "Misty's team", List.of(
                createPokemon("Staryu", 120, 30)
        ));
        when(pokemonTeamDao.getAll()).thenReturn(List.of(firstTeam, secondTeam));
        var expectedResponse = List.of(
                toTeamResponse(firstTeam),
                toTeamResponse(secondTeam)
        );

        //when
        var response = restTemplate.exchange(
                "/v1/pokemon/team", HttpMethod.GET, null, PokemonTeamResponseDto[].class);

        //then
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, List.of(response.getBody()));
    }

    @Test
    void shouldCreateMyPokemonTeamAndReturnIt() {
        //given
        when(random.ints(anyInt(), anyInt())).thenReturn(IntStream.of(1, 2, 3, 4, 5, 6));
        mockPokeApiForId(1);
        mockPokeApiForId(2);
        mockPokeApiForId(3);
        mockPokeApiForId(4);
        mockPokeApiForId(5);
        mockPokeApiForId(6);

        var expectedPokemons = createPokemons();
        var expectedTeam = new PokemonTeam(1, "Ash", expectedPokemons);
        when(pokemonTeamDao.create(eq("Ash"), anyList())).thenReturn(expectedTeam);

        //when
        var response = restTemplate.exchange("/v1/pokemon/team?name=Ash", HttpMethod.POST, null, PokemonTeamResponseDto.class);

        //then
        assertEquals(201, response.getStatusCode().value());
        assertEquals(toTeamResponse(expectedTeam), response.getBody());
    }

    @Test
    void shouldReturnConflictWhenCreatingTeamWithExistingName() {
        //given
        when(random.ints(anyInt(), anyInt()))
                .thenAnswer(invocation -> IntStream.of(1, 2, 3, 4, 5, 6));
        IntStream.rangeClosed(1, 6).forEach(this::mockPokeApiForId);

        var expectedTeam = new PokemonTeam(1, "Ash", createPokemons());
        when(pokemonTeamDao.create(eq("Ash"), anyList()))
                .thenReturn(expectedTeam)
                .thenThrow(new PokemonTeamNameAlreadyExistsException("Ash"));

        //when
        var createdResponse = restTemplate.exchange(
                "/v1/pokemon/team?name=Ash", HttpMethod.POST, null, PokemonTeamResponseDto.class);
        var duplicateResponse = restTemplate.exchange(
                "/v1/pokemon/team?name=Ash", HttpMethod.POST, null, ProblemDto.class);

        //then
        assertEquals(201, createdResponse.getStatusCode().value());
        assertEquals(toTeamResponse(expectedTeam), createdResponse.getBody());
        assertEquals(409, duplicateResponse.getStatusCode().value());
        assertEquals("A team with this name already exists.", duplicateResponse.getBody().detail());
        assertEquals("/v1/pokemon/team", duplicateResponse.getBody().instance());
    }

    @Test
    void shouldReturnMyWinnerPokemonWhenMyPokemonWinAgainstOpponent() {
        //given
        var expectedWinner = createPokemon("strongOne", 1, 1000);
        var expectedResponse = new PokemonDetailsResponseDto(1, "strongOne",
                new StatisticsResponseDto(1000, 1000, 1000, 1000));
        var myPokemonTeam = new PokemonTeam(1, "Ash", List.of(expectedWinner));
        when(pokemonTeamDao.getByName("Ash")).thenReturn(myPokemonTeam);
        when(pokemonTeamDao.getById(1)).thenReturn(myPokemonTeam);
        var opponentId = 100;
        when(random.nextInt(anyInt(), anyInt())).thenReturn(opponentId);
        when(pokeApiClient.getPokemonDetails(opponentId)).thenReturn(createPokemonDto("wekaOne", 1));

        //when
        var response = restTemplate.exchange("/v1/pokemon/team/battle?name=Ash", HttpMethod.PUT, null, PokemonDetailsResponseDto.class);

        //then
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());

    }

    @Test
    void shouldReturnOpponentPokemonWhenMyPokemonLoseAgainstOpponentAndRemoveItFromMyPokemonTeam() {
        //given
        var myWeakPokemon = createPokemon("weakOne", 1, 1);
        var expectedResponse = new PokemonDetailsResponseDto(100, "strongOne",
                new StatisticsResponseDto(1000, 1000, 1000, 1000));
        var myPokemonTeam = new PokemonTeam(1, "Ash", List.of(myWeakPokemon));
        when(pokemonTeamDao.getByName("Ash")).thenReturn(myPokemonTeam);
        when(pokemonTeamDao.getById(1)).thenReturn(myPokemonTeam);
        var opponentId = 100;
        when(random.nextInt(anyInt(), anyInt())).thenReturn(opponentId);
        var strongOpponent = createPokemonDto("strongOne", 1000);
        when(pokeApiClient.getPokemonDetails(opponentId)).thenReturn(strongOpponent);

        //when
        var response = restTemplate.exchange("/v1/pokemon/team/battle?name=Ash", HttpMethod.PUT, null, PokemonDetailsResponseDto.class);

        //then
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
        verify(pokemonTeamDao).save(new PokemonTeam(1, "Ash", List.of()));

    }

    @Test
    void shouldReturnBadRequestWhenBattleEndpointIsAskedWithoutAnyPokemonsInTeam() {
        //given
        var myPokemonTeam = new PokemonTeam(1, "Ash", List.of());
        when(pokemonTeamDao.getByName("Ash")).thenReturn(new PokemonTeam(1, "Ash", myPokemonTeam.pokemons()));

        //when
        var response = restTemplate.exchange("/v1/pokemon/team/battle?name=Ash", HttpMethod.PUT, null, ProblemDto.class);

        //then
        assertEquals(400, response.getStatusCode().value());
        assertEquals(400, response.getBody().status());
        assertEquals("/v1/pokemon/team/battle", response.getBody().instance());
    }

    private void mockPokeApiForId(int id) {
        when(pokeApiClient.getPokemonDetails(id)).thenReturn(
                PokemonDto
                                .builder()
                                .name("Pokemon" + id)
                                .stats(List.of())
                                .build()
        );
    }

    private List<Pokemon> createPokemons() {
        return List.of(
                new Pokemon(1, "Pokemon1", testStatistics()),
                new Pokemon(2, "Pokemon2", testStatistics()),
                new Pokemon(3, "Pokemon3", testStatistics()),
                new Pokemon(4, "Pokemon4", testStatistics()),
                new Pokemon(5, "Pokemon5", testStatistics()),
                new Pokemon(6, "Pokemon6", testStatistics())
        );
    }

    private Statistics testStatistics() {
        return new Statistics(1, 1, 0, 0);
    }

    private Pokemon createPokemon(String name, int id, int statsValue) {
        return new Pokemon(id, name,
                new Statistics(statsValue, statsValue, statsValue, statsValue));
    }

    private PokemonDto createPokemonDto(String name, int statsValue) {
        var hpStat = new StatisticDto("hp");
        var attackStat = new StatisticDto("attack");
        var defenseStat = new StatisticDto("defense");
        var speedStat = new StatisticDto("speed");
        var statisticsList = List.of(
                StatisticsDto.builder().baseStat(statsValue).stat(hpStat).build(),
                StatisticsDto.builder().baseStat(statsValue).stat(attackStat).build(),
                StatisticsDto.builder().baseStat(statsValue).stat(defenseStat).build(),
                StatisticsDto.builder().baseStat(statsValue).stat(speedStat).build()
        );
        return PokemonDto
                .builder()
                .name(name)
                .stats(statisticsList)
                .build();
    }

    private PokemonTeamResponseDto toTeamResponse(PokemonTeam team) {
        return new PokemonTeamResponseDto(team.id(), team.name(),
                team.pokemons().stream().map(this::toDetailsResponse).toList());
    }

    private PokemonDetailsResponseDto toDetailsResponse(Pokemon pokemon) {
        var stats = pokemon.statistics();
        var responseStats = new StatisticsResponseDto(
                stats.hp(), stats.attack(), stats.defence(), stats.speed());
        return new PokemonDetailsResponseDto(pokemon.id(), pokemon.name(), responseStats);
    }

}
