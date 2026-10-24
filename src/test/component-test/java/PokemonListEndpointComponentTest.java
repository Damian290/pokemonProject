import com.rest.pokemon.PokemonApplication;
import com.rest.pokemon.config.CacheConfig;
import com.rest.pokemon.api.dto.PokemonOverviewResponseDto;
import com.rest.pokemon.external.pokeapi.PokeApiClient;
import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.exceptionhandler.ProblemDto;
import com.rest.pokemon.external.pokeapi.dto.list.PokemonListDto;
import com.rest.pokemon.external.pokeapi.dto.list.PokemonOverviewDto;
import configuration.AbstractComponentTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.web.client.RestClientResponseException;

import java.nio.charset.Charset;
import java.util.List;

import static com.rest.pokemon.feature.pokemonlist.PokemonListService.FIRST_GEN_POKEMON_COUNT;
import static org.mockito.Mockito.*;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {PokemonApplication.class,
                AbstractComponentTestConfiguration.class}
)
@ActiveProfiles("component-test")
public class PokemonListEndpointComponentTest {

    @Autowired
    private PokeApiClient pokeApiClient;

    @Autowired
    private PokeApiConnector pokeApiConnector;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        reset(pokeApiClient);
        cacheManager.getCache(CacheConfig.POKEMON_LIST_CACHE).clear();
        cacheManager.getCache(CacheConfig.POKEMON_DETAILS_CACHE).clear();
    }

    @Test
    void shouldReturnAllPokemonsReturnedByPokeApiAndUseCacheForNextCall() {
        //given
        List<PokemonOverviewDto> mockPokemonList = List.of(
                new PokemonOverviewDto("Bulbasaur", "url1"),
                new PokemonOverviewDto("Ivisaur", "url2")
        );

        var expectedBody = List.of(
                new PokemonOverviewResponseDto("Bulbasaur", "url1"),
                new PokemonOverviewResponseDto("Ivisaur", "url2")
        );

        when(pokeApiClient.getPokemonList(FIRST_GEN_POKEMON_COUNT))
                .thenReturn(new PokemonListDto(mockPokemonList))
                .thenReturn(new PokemonListDto(List.of(new PokemonOverviewDto("Mew", "url3"))));

        //when
        var response = restTemplate.exchange("/v1/pokemon", HttpMethod.GET, null, PokemonOverviewResponseDto[].class);
        var cachedResponse = restTemplate.exchange("/v1/pokemon", HttpMethod.GET, null, PokemonOverviewResponseDto[].class);

        //then
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedBody, java.util.Arrays.asList(response.getBody()));
        assertEquals(expectedBody, java.util.Arrays.asList(cachedResponse.getBody()));
    }

    @Test
    void shouldReturnBadGatewayWhenExternalApiReturnsErrorStatus() {
        //given
        RestClientResponseException exception = new RestClientResponseException("PokeAPI error",
                HttpStatus.NOT_FOUND.value(),
                "Internal Server Error",
                HttpHeaders.EMPTY,
                new byte[0],
                Charset.defaultCharset()
        );
        when(pokeApiClient.getPokemonList(FIRST_GEN_POKEMON_COUNT))
                .thenThrow(exception);
        //when
        var response = restTemplate.exchange("/v1/pokemon", HttpMethod.GET, null, ProblemDto.class);

        //then
        assertEquals(502, response.getStatusCode().value());
        assertEquals("PokeAPI is unavailable.", response.getBody().detail());
        assertEquals("/v1/pokemon", response.getBody().instance());
    }

    @Test
    void shouldRetryWhenExternalApiReturnsServerError() {
        //given
        var exception = new RestClientResponseException(
                "PokeAPI server error",
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Service Unavailable",
                HttpHeaders.EMPTY,
                new byte[0],
                Charset.defaultCharset()
        );
        when(pokeApiClient.getPokemonList(FIRST_GEN_POKEMON_COUNT))
                .thenThrow(exception)
                .thenReturn(new PokemonListDto(List.of(new PokemonOverviewDto("Bulbasaur", "url1"))));

        //when
        var response = restTemplate.exchange("/v1/pokemon", HttpMethod.GET, null, PokemonOverviewResponseDto[].class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(new PokemonOverviewResponseDto("Bulbasaur", "url1")),
                java.util.Arrays.asList(response.getBody()));
    }

    @Test
    void shouldNotRetryWhenExternalApiReturnsNotFoundForPokemonDetails() {
        var exception = new RestClientResponseException(
                "PokeAPI resource not found",
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                HttpHeaders.EMPTY,
                new byte[0],
                Charset.defaultCharset()
        );
        when(pokeApiClient.getPokemonDetails(25))
                .thenThrow(exception)
                .thenReturn(new PokemonDto("Pikachu", List.of()));

        var thrown = assertThrows(com.rest.pokemon.external.pokeapi.PokeApiPokemonDetailsException.class,
                () -> pokeApiConnector.getPokemonDetails(25));

        assertSame(exception, thrown.getCause());
    }


}
