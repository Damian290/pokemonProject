package com.rest.pokemon.service.feature.pokemonlist;

import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PokemonListServiceConfig {

    @Bean
    public PokemonListService pokemonListService(
            PokeApiConnector pokeApiConnector
    ) {
        return new PokemonListService(pokeApiConnector);
    }
}
