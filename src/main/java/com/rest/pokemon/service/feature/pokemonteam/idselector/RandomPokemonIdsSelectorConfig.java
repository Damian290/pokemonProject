package com.rest.pokemon.service.feature.pokemonteam.idselector;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class RandomPokemonIdsSelectorConfig {

    @Bean
    public RandomPokemonIdsSelector randomPokemonIdsSelector(
            Random random
    ) {
        return new RandomPokemonIdsSelector(random);
    }

    @Bean
    public Random random() {
        return new Random();
    }
}
