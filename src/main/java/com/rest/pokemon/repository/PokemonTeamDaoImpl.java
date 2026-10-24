package com.rest.pokemon.repository;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.repository.entity.PokemonTeamEntity;
import com.rest.pokemon.repository.exception.PokemonTeamNameAlreadyExistsException;
import com.rest.pokemon.repository.exception.PokemonTeamNotFoundException;
import com.rest.pokemon.repository.mapper.PokemonDetailsEntityMapper;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PokemonTeamDaoImpl implements PokemonTeamDao {

    // In-memory stand-in for the database table storing Pokemon teams.
    private final List<PokemonTeamEntity> pokemonTeams = new ArrayList<>();
    private final PokemonDetailsEntityMapper mapper;
    private long nextId = 1;

    @Override
    public synchronized PokemonTeam create(String name, List<PokemonDetails> newPokemonDetails) {
        if (pokemonTeams.stream().anyMatch(team -> team.name().equals(name))) {
            throw new PokemonTeamNameAlreadyExistsException(name);
        }
        var entities = newPokemonDetails.stream().map(mapper::toEntity).toList();
        var entity = new PokemonTeamEntity(nextId++, name, entities);
        pokemonTeams.add(entity);
        return toDomain(entity);
    }

    @Override
    public synchronized List<PokemonTeam> getAll() {
        return pokemonTeams.stream().map(this::toDomain).toList();
    }

    @Override
    public synchronized PokemonTeam getById(long teamId) {
        return toDomain(findTeam(teamId));
    }

    @Override
    public synchronized PokemonTeam getByName(String teamName) {
        var team = pokemonTeams.stream()
                .filter(candidate -> candidate.name().equals(teamName))
                .findFirst()
                .orElseThrow(() -> new PokemonTeamNotFoundException(teamName));
        return toDomain(team);
    }

    @Override
    public synchronized PokemonDetails removeFirst(long teamId) {
        var team = findTeam(teamId);
        var pokemonDetails = new ArrayList<>(team.pokemonDetails());
        var removedPokemon = mapper.toDomain(pokemonDetails.removeFirst());
        pokemonTeams.set(pokemonTeams.indexOf(team), new PokemonTeamEntity(team.id(), team.name(), pokemonDetails));
        return removedPokemon;
    }

    @Override
    public synchronized void updateFirstPokemon(long teamId, PokemonDetails updatedPokemon) {
        var team = findTeam(teamId);
        var pokemonDetails = new ArrayList<>(team.pokemonDetails());
        pokemonDetails.removeFirst();
        pokemonDetails.addFirst(mapper.toEntity(updatedPokemon));
        pokemonTeams.set(pokemonTeams.indexOf(team), new PokemonTeamEntity(team.id(), team.name(), pokemonDetails));
    }

    private PokemonTeamEntity findTeam(long teamId) {
        return pokemonTeams.stream()
                .filter(team -> team.id() == teamId)
                .findFirst()
                .orElseThrow(() -> new PokemonTeamNotFoundException(String.valueOf(teamId)));
    }

    private PokemonTeam toDomain(PokemonTeamEntity entity) {
        return new PokemonTeam(
                entity.id(),
                entity.name(),
                entity.pokemonDetails().stream().map(mapper::toDomain).toList()
        );
    }
}
