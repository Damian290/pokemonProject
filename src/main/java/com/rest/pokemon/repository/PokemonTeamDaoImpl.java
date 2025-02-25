package com.rest.pokemon.repository;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PokemonTeamDaoImpl implements PokemonTeamDao {

    private final PokemonTeam myPokemonTeam;

    @Override
    public synchronized PokemonTeam create(List<PokemonDetails> newPokemonDetails) {
        myPokemonTeam.pokemonDetails().clear();
        myPokemonTeam.pokemonDetails().addAll(newPokemonDetails);
        return copyMyPokemonTeam();
    }

    @Override
    public synchronized PokemonTeam getAll() {
        return copyMyPokemonTeam();
    }

    @Override
    public synchronized PokemonDetails removeFirst() {
        return myPokemonTeam.pokemonDetails().removeFirst().copy();
    }

    @Override
    public synchronized void updateFirstPokemon(PokemonDetails updatedPokemon) {
        myPokemonTeam.pokemonDetails().removeFirst();
        myPokemonTeam.pokemonDetails().addFirst(updatedPokemon);
    }

    private PokemonTeam copyMyPokemonTeam() {
        var copiedList = myPokemonTeam.pokemonDetails().stream()
                .map(PokemonDetails::copy)
                .collect(Collectors.toList());

        return new PokemonTeam(copiedList);
    }
}
