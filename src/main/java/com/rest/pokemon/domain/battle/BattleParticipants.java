package com.rest.pokemon.domain.battle;

import com.rest.pokemon.domain.pokemon.PokemonDetails;

public record BattleParticipants(
        long teamId,
        PokemonDetails myPokemon,
        PokemonDetails opponentPokemon
) {
}
