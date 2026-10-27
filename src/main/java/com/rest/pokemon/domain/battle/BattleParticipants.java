package com.rest.pokemon.domain.battle;

import com.rest.pokemon.domain.pokemon.Pokemon;

public record BattleParticipants(
        long teamId,
        Pokemon myPokemon,
        Pokemon opponentPokemon
) {
}
