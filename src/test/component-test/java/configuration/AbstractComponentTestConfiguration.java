package configuration;

import com.rest.pokemon.external.pokeapi.PokeApiClient;
import com.rest.pokemon.repository.PokemonTeamDao;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Random;

@Configuration
public class AbstractComponentTestConfiguration {

    @Bean
    @Primary
    public Random random() {
        return Mockito.mock(Random.class);
    }

    @Bean
    @Primary
    public PokeApiClient pokeApiClient() {
        return Mockito.mock(PokeApiClient.class);
    }

    @Bean
    @Primary
    public PokemonTeamDao pokemonTeamDao() {
        return Mockito.mock(PokemonTeamDao.class);
    }

}
