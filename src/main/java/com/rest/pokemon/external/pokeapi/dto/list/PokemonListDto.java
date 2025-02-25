package com.rest.pokemon.external.pokeapi.dto.list;

import java.util.List;

public record PokemonListDto(
        List<PokemonOverviewDto> results
) {
}
