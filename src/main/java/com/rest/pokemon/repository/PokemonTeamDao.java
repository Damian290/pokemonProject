package com.rest.pokemon.repository;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.PokemonTeam;

import java.util.List;

public interface PokemonTeamDao {

    PokemonTeam create(String name, List<PokemonDetails> pokemonDetails);

    List<PokemonTeam> getAll();

    PokemonTeam getById(long teamId);

    PokemonTeam getByName(String teamName);

    PokemonDetails removeFirst(long teamId);

    void updateFirstPokemon(long teamId, PokemonDetails pokemonDetails);
}
