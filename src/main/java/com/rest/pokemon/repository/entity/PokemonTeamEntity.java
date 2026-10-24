package com.rest.pokemon.repository.entity;

import java.util.List;

public record PokemonTeamEntity(
        long id,
        String name,
        List<PokemonDetailsEntity> pokemonDetails
) {
}
