package com.rest.pokemon.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PokemonTeamDaoConfig {

    @Bean
    public PokemonTeamDao pokemonTeamDao(
            JpaPokemonTeamRepository teamRepository,
            JpaPokemonEntityMapper mapper
    ) {
        return new JpaPokemonTeamDao(teamRepository, mapper);
    }
}
