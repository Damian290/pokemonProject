package com.rest.pokemon.feature.pokemonlist;

import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.mapper.PokemonOverviewDtoToPokemonOverviewMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PokemonListServiceConfig {

    @Bean
    public PokemonListService pokemonListService(
            PokeApiConnector pokeApiConnector,
            PokemonOverviewDtoToPokemonOverviewMapper pokemonMapper
    ) {
        return new PokemonListService(pokeApiConnector, pokemonMapper);
    }
}
