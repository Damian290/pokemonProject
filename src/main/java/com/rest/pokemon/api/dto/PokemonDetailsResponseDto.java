package com.rest.pokemon.api.dto;

public record PokemonDetailsResponseDto(Integer id, String name, StatisticsResponseDto statistics) {
}
