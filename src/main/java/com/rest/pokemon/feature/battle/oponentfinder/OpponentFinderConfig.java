package com.rest.pokemon.feature.battle.oponentfinder;

import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.mapper.PokemonDtoToPokemonDetailsMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class OpponentFinderConfig {

    @Bean
    public OpponentFinder opponentFinder(
            PokeApiConnector pokeApiConnector,
            Random random,
            PokemonDtoToPokemonDetailsMapper pokemonMapper
    ) {
        return new OpponentFinder(pokeApiConnector, pokemonMapper, random);
    }
}
