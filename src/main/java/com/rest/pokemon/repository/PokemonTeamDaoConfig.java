package com.rest.pokemon.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class PokemonTeamDaoConfig {

    @Bean
    public PokemonTeamDao pokemonTeamDao() {
        return new PokemonTeamDaoImpl(new PokemonTeam(new ArrayList<>()));
    }
}
