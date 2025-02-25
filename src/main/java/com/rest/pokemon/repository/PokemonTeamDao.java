package com.rest.pokemon.repository;

import com.rest.pokemon.domain.pokemon.PokemonDetails;

import java.util.List;

public interface PokemonTeamDao {

    PokemonTeam create(List<PokemonDetails> pokemonDetails);

    PokemonTeam getAll();

    PokemonDetails removeFirst();

    void updateFirstPokemon(PokemonDetails pokemonDetails);
}
