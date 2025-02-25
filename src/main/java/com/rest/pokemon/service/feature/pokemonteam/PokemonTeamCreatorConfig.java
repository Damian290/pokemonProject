package com.rest.pokemon.service.feature.pokemonteam;

import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.repository.PokemonTeamDao;
import com.rest.pokemon.service.feature.pokemonteam.idselector.RandomPokemonIdsSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PokemonTeamCreatorConfig {

    @Bean
    public PokemonTeamCreator pokemonTeamCreator(
            PokeApiConnector pokeApiConnector,
            PokemonTeamDao pokemonTeamDao,
            RandomPokemonIdsSelector randomPokemonTeamSelector
    ) {
        return new PokemonTeamCreator(
                randomPokemonTeamSelector,
                pokeApiConnector,
                pokemonTeamDao
        );
    }


}
