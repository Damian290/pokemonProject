package com.rest.pokemon.repository.entity;

public record PokemonEntity(
        Integer id,
        String name,
        long hp,
        long attack,
        long defence,
        long speed
) {
}
