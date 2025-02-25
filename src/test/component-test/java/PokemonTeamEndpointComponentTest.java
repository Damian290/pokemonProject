import com.rest.pokemon.PokemonApplication;
import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.external.pokeapi.PokeApiClient;
import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticsDto;
import com.rest.pokemon.repository.PokemonTeam;
import com.rest.pokemon.repository.PokemonTeamDao;
import configuration.AbstractComponentTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

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

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PokemonTeamDao pokemonTeamDao;

    @Autowired
    private PokeApiClient pokeApiClient;

    @Autowired
    private Random random;

    @BeforeEach
    void setUp() {
        reset(random, pokeApiClient, pokemonTeamDao);
    }

    @Test
    void shouldReturnMyPokemonTeam() {
        //given
        var myFirstPokemon = PokemonDetails.builder().id(1).build();
        var mySecondPokemon = PokemonDetails.builder().id(2).build();

        var pokemonTeam = new PokemonTeam(
                List.of(myFirstPokemon, mySecondPokemon)
        );

        when(pokemonTeamDao.getAll()).thenReturn(pokemonTeam);

        //when
        var response = webTestClient.get()
                .uri("/v1/pokemon/team")
                .exchange();

        //then
        response.expectStatus().isOk()
                .expectBody(PokemonTeam.class)
                .isEqualTo(pokemonTeam);

        verify(pokemonTeamDao, times(1)).getAll();
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

        var expectedList = createPokemonDetailsList();
        var expectedPokemonTeam = new PokemonTeam(expectedList);
        when(pokemonTeamDao.create(expectedList)).thenReturn(expectedPokemonTeam);

        //when
        var response = webTestClient.post()
                .uri("/v1/pokemon/team")
                .exchange();

        //then
        response.expectStatus().isOk()
                .expectBody(PokemonTeam.class)
                .isEqualTo(expectedPokemonTeam);

        verify(pokemonTeamDao, times(1)).create(expectedList);
    }

    @Test
    void shouldReturnMyWinnerPokemonWhenMyPokemonWinAgainstOpponent() {
        //given
        var expectedWinner = createPokemonDetails("strongOne", 1, 1000);
        var myPokemonTeam = new PokemonTeam(
                List.of(expectedWinner)
        );

        when(pokemonTeamDao.getAll()).thenReturn(myPokemonTeam);
        var opponentId = 100;
        when(random.nextInt(anyInt(), anyInt())).thenReturn(opponentId);
        when(pokeApiClient.getPokemonDetails(opponentId)).thenReturn(Mono.just(createPokemonDto("wekaOne", 1)));

        //when
        var response = webTestClient.put()
                .uri("/v1/pokemon/team/battle")
                .exchange();

        //then
        response.expectStatus().isOk()
                .expectBody(PokemonDetails.class).isEqualTo(expectedWinner);

        verify(pokemonTeamDao, times(1)).getAll();
        verify(random, times(1)).nextInt(anyInt(), anyInt());
        verify(pokeApiClient, times(1)).getPokemonDetails(opponentId);
        verify(pokemonTeamDao,times(1)).updateFirstPokemon(expectedWinner);
        verify(pokemonTeamDao, times(0)).removeFirst();
    }

    @Test
    void shouldReturnOpponentPokemonWhenMyPokemonLoseAgainstOpponentAndRemoveItFromMyPokemonTeam() {
        //given
        var myWeakPokemon = createPokemonDetails("weakOne", 1, 1);
        var myPokemonTeam = new PokemonTeam(
                List.of(myWeakPokemon)
        );

        when(pokemonTeamDao.getAll()).thenReturn(myPokemonTeam);
        var opponentId = 100;
        when(random.nextInt(anyInt(), anyInt())).thenReturn(opponentId);
        var strongOpponent = createPokemonDto("strongOne", 1000);
        var expectedWinner = createPokemonDetails("strongOne", opponentId, 1000);
        when(pokeApiClient.getPokemonDetails(opponentId)).thenReturn(Mono.just(strongOpponent));

        //when
        var response = webTestClient.put()
                .uri("/v1/pokemon/team/battle")
                .exchange();

        //then
        response.expectStatus().isOk()
                .expectBody(PokemonDetails.class).isEqualTo(expectedWinner);

        verify(pokemonTeamDao, times(1)).getAll();
        verify(random, times(1)).nextInt(anyInt(), anyInt());
        verify(pokeApiClient, times(1)).getPokemonDetails(opponentId);
        verify(pokemonTeamDao, times(1)).removeFirst();
    }

    @Test
    void shouldReturnBadRequestWhenBattleEndpointIsAskedWithoutAnyPokemonsInTeam() {
        //given
        var myPokemonTeam = new PokemonTeam(List.of());
        when(pokemonTeamDao.getAll()).thenReturn(myPokemonTeam);

        //when
        var response = webTestClient.put()
                .uri("/v1/pokemon/team/battle")
                .exchange();

        //then
        response.expectStatus().isBadRequest();
    }

    private void mockPokeApiForId(int id) {
        when(pokeApiClient.getPokemonDetails(id)).thenReturn(
                Mono.just(
                        PokemonDto
                                .builder()
                                .name("Pokemon" + id)
                                .stats(List.of())
                                .build()
                )
        );
    }

    private List<PokemonDetails> createPokemonDetailsList() {
        return List.of(
                PokemonDetails.builder().id(1).name("Pokemon1").statistics(Statistics.builder().build()).build(),
                PokemonDetails.builder().id(2).name("Pokemon2").statistics(Statistics.builder().build()).build(),
                PokemonDetails.builder().id(3).name("Pokemon3").statistics(Statistics.builder().build()).build(),
                PokemonDetails.builder().id(4).name("Pokemon4").statistics(Statistics.builder().build()).build(),
                PokemonDetails.builder().id(5).name("Pokemon5").statistics(Statistics.builder().build()).build(),
                PokemonDetails.builder().id(6).name("Pokemon6").statistics(Statistics.builder().build()).build()
        );
    }

    private PokemonDetails createPokemonDetails(String name, int id, int statsValue) {
        var statistics = Statistics.builder()
                .hp(statsValue)
                .attack(statsValue)
                .defence(statsValue)
                .speed(statsValue)
                .build();
        return PokemonDetails
                .builder()
                .id(id)
                .name(name)
                .statistics(statistics)
                .build();
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

}
