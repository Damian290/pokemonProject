package com.rest.pokemon.service.feature.battle.oponentfinder;

import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class OpponentFinderConfig {

    @Bean
    public OpponentFinder opponentFinder(
            PokeApiConnector pokeApiConnector,
            Random random
    ) {
        return new OpponentFinder(pokeApiConnector, random);
    }
}
