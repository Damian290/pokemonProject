package com.rest.pokemon.domain.pokemon;

import lombok.Builder;

@Builder
public record PokemonOverview(
        String name,
        String url
){
}
