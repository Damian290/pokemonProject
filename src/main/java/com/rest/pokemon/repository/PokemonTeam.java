package com.rest.pokemon.repository;

import com.rest.pokemon.domain.pokemon.PokemonDetails;

import java.util.List;

//represent sharable object - db replacement in this project
public record PokemonTeam(
        List<PokemonDetails> pokemonDetails
) {
}
