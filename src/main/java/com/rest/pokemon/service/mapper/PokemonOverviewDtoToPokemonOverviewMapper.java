package com.rest.pokemon.service.mapper;

import com.rest.pokemon.domain.pokemon.PokemonOverview;
import com.rest.pokemon.external.pokeapi.dto.list.PokemonOverviewDto;

public class PokemonOverviewDtoToPokemonOverviewMapper {

    public PokemonOverview map(PokemonOverviewDto pokemonDto) {
        return PokemonOverview.builder()
                .name(pokemonDto.name())
                .url(pokemonDto.url())
                .build();
    }
}
