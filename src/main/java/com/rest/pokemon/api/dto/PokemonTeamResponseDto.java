package com.rest.pokemon.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PokemonTeamResponseDto(
        long id,
        String name,
        @JsonProperty("pokemonDetails") List<PokemonDetailsResponseDto> pokemons
) {
}
