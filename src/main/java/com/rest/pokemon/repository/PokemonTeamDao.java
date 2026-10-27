package com.rest.pokemon.repository;

import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.PokemonTeam;

import java.util.List;

public interface PokemonTeamDao {

    PokemonTeam create(String name, List<Pokemon> pokemons);

    List<PokemonTeam> getAll();

    PokemonTeam getById(long teamId);

    PokemonTeam getByName(String teamName);

    void save(PokemonTeam team);
}
