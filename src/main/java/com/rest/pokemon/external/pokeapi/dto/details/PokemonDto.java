package com.rest.pokemon.external.pokeapi.dto.details;

import lombok.Builder;

import java.util.List;

@Builder
public record PokemonDto(
        String name,
        List<StatisticsDto> stats
) {
}
