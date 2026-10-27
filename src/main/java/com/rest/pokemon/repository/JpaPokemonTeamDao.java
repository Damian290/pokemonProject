package com.rest.pokemon.repository;

import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.repository.entity.jpa.JpaPokemonTeamEntity;
import com.rest.pokemon.repository.exception.PokemonTeamNameAlreadyExistsException;
import com.rest.pokemon.repository.exception.PokemonTeamNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;

public class JpaPokemonTeamDao implements PokemonTeamDao {

    private final JpaPokemonTeamRepository teamRepository;
    private final JpaPokemonEntityMapper mapper;

    public JpaPokemonTeamDao(JpaPokemonTeamRepository teamRepository, JpaPokemonEntityMapper mapper) {
        this.teamRepository = teamRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public PokemonTeam create(PokemonTeam team) {
        if (team.id() != null) {
            throw new IllegalArgumentException("New team must not have an ID");
        }

        if (teamRepository.existsByName(team.name())) {
            throw new PokemonTeamNameAlreadyExistsException(team.name());
        }

        var entity = new JpaPokemonTeamEntity(team.name());
        entity.replacePokemons(
                team.pokemons().stream()
                        .map(mapper::toEntity)
                        .toList()
        );

        return mapper.toDomain(teamRepository.save(entity));
    }

    @Override
    @Transactional
    public List<PokemonTeam> getAll() {
        return teamRepository.findAllByOrderByIdAsc().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public PokemonTeam getById(long teamId) {
        return mapper.toDomain(findTeam(teamId));
    }

    @Override
    @Transactional
    public PokemonTeam getByName(String teamName) {
        var entity = teamRepository.findByName(teamName)
                .orElseThrow(() -> new PokemonTeamNotFoundException(teamName));
        return mapper.toDomain(entity);
    }

    @Override
    @Transactional
    public void save(PokemonTeam team) {
        if (team.id() == null) {
            throw new IllegalArgumentException("Team ID must be assigned before saving");
        }
        var entity = findTeam(team.id());
        entity.setName(team.name());
        entity.replacePokemons(team.pokemons().stream().map(mapper::toEntity).toList());
        teamRepository.save(entity);
    }

    private JpaPokemonTeamEntity findTeam(long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new PokemonTeamNotFoundException(String.valueOf(teamId)));
    }

}
