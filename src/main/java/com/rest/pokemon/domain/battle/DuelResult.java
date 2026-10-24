package com.rest.pokemon.domain.battle;

import com.rest.pokemon.domain.pokemon.PokemonDetails;

public record DuelResult(
        PokemonDetails winner,
        boolean myPokemonWinner
) {
}
