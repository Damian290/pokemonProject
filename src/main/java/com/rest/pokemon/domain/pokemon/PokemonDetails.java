package com.rest.pokemon.domain.pokemon;

import lombok.Builder;

@Builder
public record PokemonDetails (
        Integer id,
        String name,
        Statistics statistics
) {

}