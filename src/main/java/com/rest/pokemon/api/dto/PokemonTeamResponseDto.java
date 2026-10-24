package com.rest.pokemon.api.dto;

import java.util.List;

public record PokemonTeamResponseDto(long id, String name, List<PokemonDetailsResponseDto> pokemonDetails) {
}
