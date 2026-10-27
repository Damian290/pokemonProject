package com.rest.pokemon.repository;

import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.repository.entity.PokemonTeamEntity;
import com.rest.pokemon.repository.exception.PokemonTeamNameAlreadyExistsException;
import com.rest.pokemon.repository.exception.PokemonTeamNotFoundException;
import com.rest.pokemon.repository.mapper.PokemonEntityMapper;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PokemonTeamDaoImpl implements PokemonTeamDao {

    // In-memory stand-in for the database table storing Pokemon teams.
    private final List<PokemonTeamEntity> pokemonTeams = new ArrayList<>();
    private final PokemonEntityMapper mapper;
    private long nextId = 1;

    @Override
    public synchronized PokemonTeam create(String name, List<Pokemon> newPokemons) {
        if (pokemonTeams.stream().anyMatch(team -> team.name().equals(name))) {
            throw new PokemonTeamNameAlreadyExistsException(name);
        }
        var team = new PokemonTeam(nextId, name, newPokemons);
        var entity = toEntity(team);
        pokemonTeams.add(entity);
        nextId++;
        return team;
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
    public synchronized void save(PokemonTeam team) {
        var existing = findTeam(team.id());
        pokemonTeams.set(pokemonTeams.indexOf(existing), toEntity(team));
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
                entity.pokemons().stream().map(mapper::toDomain).toList()
        );
    }

    private PokemonTeamEntity toEntity(PokemonTeam team) {
        return new PokemonTeamEntity(
                team.id(),
                team.name(),
                team.pokemons().stream().map(mapper::toEntity).toList()
        );
    }
}
