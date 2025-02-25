package com.rest.pokemon.service.feature.pokemonteam;

import com.rest.pokemon.repository.PokemonTeamDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PokemonTeamServiceConfig {

    @Bean
    public PokemonTeamService pokemonTeamService(
            PokemonTeamDao pokemonTeamDao
    ) {
        return new PokemonTeamService(pokemonTeamDao);
    }
}
