package com.rest.pokemon.domain.pokemon;

import lombok.Builder;

@Builder
public record PokemonDetails(
        Integer id,
        String name,
        Statistics statistics
) {

    public PokemonDetails copy() {
        return new PokemonDetails(
                this.id,
                this.name,
                Statistics.builder()
                        .hp(this.statistics.getHp())
                        .attack(this.statistics.getAttack())
                        .defence(this.statistics.getDefence())
                        .speed(this.statistics.getSpeed())
                        .build()
        );
    }
}