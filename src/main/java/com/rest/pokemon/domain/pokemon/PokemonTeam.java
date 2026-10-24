package com.rest.pokemon.domain.pokemon;

import java.util.List;

public record PokemonTeam(
        long id,
        String name,
        List<PokemonDetails> pokemonDetails
) {
    public PokemonTeam(List<PokemonDetails> pokemonDetails) {
        this(0, "", pokemonDetails);
    }
}
