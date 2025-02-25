package external.pokeapi;

import com.rest.pokemon.PokemonApplication;
import com.rest.pokemon.external.pokeapi.PokeApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = {PokemonApplication.class}
)
public class PokeApiClientIntegrationTest {

    @Autowired
    private PokeApiClient pokeApiClient;

    @Test
    void shouldReturnListOfPokemon() {
        //given
        var pokemonNr = 151;
        //when
        var result = pokeApiClient.getPokemonList(pokemonNr).block();

        //then
        assertNotNull(result);
        assertNotNull(result.results());
        assertEquals(pokemonNr, result.results().size());
    }

    @Test
    void shouldReturnPokemonDetails() {
        //given
        var pokemonNr = 1;
        //when
        var result = pokeApiClient.getPokemonDetails(pokemonNr).block();

        //then
        assertNotNull(result);
        assertNotNull(result.name());
        assertNotEquals(0, result.stats().size());
    }
}
