package com.rest.pokemon.domain.battle;

import com.rest.pokemon.domain.pokemon.Pokemon;

public record DuelResult(
        Pokemon winner,
        Pokemon defeated,
        boolean myPokemonWinner
) {
}
