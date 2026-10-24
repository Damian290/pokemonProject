package com.rest.pokemon.repository.mapper;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.repository.entity.PokemonDetailsEntity;
import org.springframework.stereotype.Component;

@Component
public class PokemonDetailsEntityMapper {

    public PokemonDetailsEntity toEntity(PokemonDetails pokemonDetails) {
        var statistics = pokemonDetails.statistics();
        return new PokemonDetailsEntity(
                pokemonDetails.id(),
                pokemonDetails.name(),
                statistics.getHp(),
                statistics.getAttack(),
                statistics.getDefence(),
                statistics.getSpeed()
        );
    }

    public PokemonDetails toDomain(PokemonDetailsEntity entity) {
        return new PokemonDetails(
                entity.id(),
                entity.name(),
                new Statistics(entity.hp(), entity.attack(), entity.defence(), entity.speed())
        );
    }
}
