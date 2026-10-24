package com.rest.pokemon.repository;

import com.rest.pokemon.repository.mapper.PokemonDetailsEntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PokemonTeamDaoConfig {

    @Bean
    public PokemonTeamDao pokemonTeamDao(
            PokemonDetailsEntityMapper mapper
    ) {
        return new PokemonTeamDaoImpl(mapper);
    }
}
