package com.rest.pokemon.feature.pokemonteam.idselector;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class RandomPokemonIdsSelector implements PokemonIdsSelector {

    private final Random random;

    @Override
    public List<Integer> selectSixIds() {
        return random.ints(1, 152)
                .distinct()
                .limit(6)
                .boxed()
                .toList();
    }
}
