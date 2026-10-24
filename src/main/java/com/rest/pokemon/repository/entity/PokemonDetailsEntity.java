package com.rest.pokemon.repository.entity;

public record PokemonDetailsEntity(
        Integer id,
        String name,
        long hp,
        long attack,
        long defence,
        long speed
) {
}
