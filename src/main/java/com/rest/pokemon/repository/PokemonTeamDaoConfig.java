package com.rest.pokemon.repository;

import com.rest.pokemon.repository.mapper.PokemonEntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PokemonTeamDaoConfig {

    @Bean
    public PokemonTeamDao pokemonTeamDao(
            PokemonEntityMapper mapper
    ) {
        return new PokemonTeamDaoImpl(mapper);
    }
}
