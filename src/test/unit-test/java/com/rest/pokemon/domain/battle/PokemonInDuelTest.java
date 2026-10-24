package com.rest.pokemon.domain.battle;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PokemonInDuelTest {

    @Test
    void shouldUpdateDefenceLevel() {
        //given
        var pokemon = new PokemonInDuel(new PokemonDetails(1, "Bulbasaur", null), 49);

        //when
        pokemon.setCurrentDefenceLvl(30);

        //then
        assertEquals(30, pokemon.getCurrentDefenceLvl());
    }

    @Test
    void shouldClampNegativeDefenceLevelToZero() {
        //given
        var pokemon = new PokemonInDuel(new PokemonDetails(1, "Bulbasaur", null), 49);

        //when
        pokemon.setCurrentDefenceLvl(-10);

        //then
        assertEquals(0, pokemon.getCurrentDefenceLvl());
    }

    @Test
    void shouldUpdateCurrentHp() {
        //given
        var details = new PokemonDetails(1, "Bulbasaur", new Statistics(45, 49, 49, 45));
        var pokemon = new PokemonInDuel(details, 49);

        //when
        pokemon.setCurrentHp(30);

        //then
        assertEquals(30, details.statistics().getHp());
    }

    @Test
    void shouldClampNegativeCurrentHpToZero() {
        //given
        var details = new PokemonDetails(1, "Bulbasaur", new Statistics(45, 49, 49, 45));
        var pokemon = new PokemonInDuel(details, 49);

        //when
        pokemon.setCurrentHp(-10);

        //then
        assertEquals(0, details.statistics().getHp());
    }
}
