import com.rest.pokemon.PokemonApplication;
import com.rest.pokemon.config.RetryConfig;
import com.rest.pokemon.domain.pokemon.PokemonOverview;
import com.rest.pokemon.external.pokeapi.PokeApiClient;
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
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.List;

import static com.rest.pokemon.service.feature.pokemonlist.PokemonListService.POKEMON_NR;
import static org.mockito.Mockito.*;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {PokemonApplication.class,
                AbstractComponentTestConfiguration.class,
                RetryConfig.class}
)
@ActiveProfiles("component-test")
public class PokemonListEndpointComponentTest {

    @Autowired
    private PokeApiClient pokeApiClient;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        reset(pokeApiClient);
    }

    @Test
    void shouldReturnAllPokemonsReturnedByPokeApi() {
        //given
        List<PokemonOverviewDto> mockPokemonList = List.of(
                new PokemonOverviewDto("Bulbasaur", "url1"),
                new PokemonOverviewDto("Ivisaur", "url2")
        );

        var expectedBody = List.of(
                new PokemonOverview("Bulbasaur", "url1"),
                new PokemonOverview("Ivisaur", "url2")
        );

        when(pokeApiClient.getPokemonList(POKEMON_NR))
                .thenReturn(Mono.just(new PokemonListDto(mockPokemonList)));

        //when
        var response = webTestClient.get()
                .uri("/v1/pokemon")
                .exchange();
        //then
        response.expectStatus().isOk()
                .expectBodyList(PokemonOverview.class)
                .isEqualTo(expectedBody);
        verify(pokeApiClient, times(1)).getPokemonList(POKEMON_NR);
    }

    @Test
    void shouldReturnExceptionReturnedByExternalApiWhenExternalApiReturnsErrorStatus() {
        //given
        WebClientResponseException exception = WebClientResponseException.create(
                HttpStatus.NOT_FOUND.value(),
                "Internal Server Error",
                HttpHeaders.EMPTY,
                new byte[0],
                Charset.defaultCharset()
        );
        when(pokeApiClient.getPokemonList(POKEMON_NR))
                .thenAnswer(invocation -> Mono.error(exception));
        //when
        var response = webTestClient.get()
                .uri("/v1/pokemon")
                .exchange();

        //then
        response.expectStatus().isNotFound();
        verify(pokeApiClient, times(1)).getPokemonList(POKEMON_NR);
    }

}

