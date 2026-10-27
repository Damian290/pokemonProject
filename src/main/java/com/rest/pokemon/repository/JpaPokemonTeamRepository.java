package com.rest.pokemon.repository;

import com.rest.pokemon.repository.entity.jpa.JpaPokemonTeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface JpaPokemonTeamRepository extends JpaRepository<JpaPokemonTeamEntity, Long> {
    Optional<JpaPokemonTeamEntity> findByName(String name);
    boolean existsByName(String name);
    List<JpaPokemonTeamEntity> findAllByOrderByIdAsc();
}
