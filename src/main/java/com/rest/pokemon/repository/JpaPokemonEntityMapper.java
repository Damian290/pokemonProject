package com.rest.pokemon.repository;

import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.repository.entity.jpa.JpaPokemonEntity;
import com.rest.pokemon.repository.entity.jpa.JpaPokemonTeamEntity;
import org.springframework.stereotype.Component;

@Component
public class JpaPokemonEntityMapper {

    public JpaPokemonEntity toEntity(Pokemon pokemon) {
        var stats = pokemon.statistics();
        return new JpaPokemonEntity(pokemon.id(), pokemon.name(), stats.hp(), stats.attack(), stats.defence(), stats.speed());
    }

    public PokemonTeam toDomain(JpaPokemonTeamEntity entity) {
        return new PokemonTeam(entity.getId(), entity.getName(), entity.getPokemons().stream()
                .map(pokemon -> new Pokemon(pokemon.getPokemonId(), pokemon.getName(),
                        new Statistics(pokemon.getHp(), pokemon.getAttack(), pokemon.getDefence(), pokemon.getSpeed())))
                .toList());
    }
}
