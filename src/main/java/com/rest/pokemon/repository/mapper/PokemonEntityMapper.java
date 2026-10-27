package com.rest.pokemon.repository.mapper;

import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.repository.entity.PokemonEntity;
import org.springframework.stereotype.Component;

@Component
public class PokemonEntityMapper {

    public PokemonEntity toEntity(Pokemon pokemon) {
        var statistics = pokemon.statistics();
        return new PokemonEntity(
                pokemon.id(),
                pokemon.name(),
                statistics.hp(),
                statistics.attack(),
                statistics.defence(),
                statistics.speed()
        );
    }

    public Pokemon toDomain(PokemonEntity entity) {
        return new Pokemon(
                entity.id(),
                entity.name(),
                new Statistics(entity.hp(), entity.attack(), entity.defence(), entity.speed())
        );
    }
}
