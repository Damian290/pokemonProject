package com.rest.pokemon.domain.pokemon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {

    @Test
    void shouldConsumeDefenceBeforeHpAndNeverMakeEitherNegative() {
        //given
        var pokemon = new Pokemon(1, "Bulbasaur", new Statistics(45, 49, 10, 45));

        //when
        pokemon.receiveDamage(20);
        pokemon.receiveDamage(50);

        //then
        assertEquals(0, pokemon.statistics().hp());
        assertTrue(pokemon.isDead());
    }

    @Test
    void shouldPreserveHpAndRestoreDefenceAfterDuel() {
        //given
        var pokemon = new Pokemon(1, "Bulbasaur", new Statistics(45, 49, 10, 45));

        //when
        pokemon.receiveDamage(10);
        pokemon.receiveDamage(15);
        var afterDuel = pokemon.afterDuel();

        //then
        assertEquals(new Statistics(30, 49, 10, 45), afterDuel.statistics());
        afterDuel.receiveDamage(5);
        assertEquals(30, afterDuel.statistics().hp());
    }

    @Test
    void shouldRejectInvalidInitialStatisticsAndNegativeDamage() {
        //given
        var validStatistics = new Statistics(45, 49, 10, 45);
        var pokemon = new Pokemon(1, "Bulbasaur", validStatistics);

        //when
        var invalidHp = assertThrows(IllegalArgumentException.class,
                () -> new Pokemon(1, "Bulbasaur", new Statistics(0, 49, 10, 45)));
        var invalidAttack = assertThrows(IllegalArgumentException.class,
                () -> new Pokemon(1, "Bulbasaur", new Statistics(45, 0, 10, 45)));
        var invalidDamage = assertThrows(IllegalArgumentException.class, () -> pokemon.receiveDamage(-1));

        //then
        assertNotNull(invalidHp.getMessage());
        assertNotNull(invalidAttack.getMessage());
        assertNotNull(invalidDamage.getMessage());
        assertEquals(validStatistics, pokemon.statistics());
    }

    @Test
    void shouldKeepCopyIndependentOfOriginalState() {
        //given
        var pokemon = new Pokemon(1, "Bulbasaur", new Statistics(45, 49, 10, 45));
        var copy = pokemon.copy();

        //when
        copy.receiveDamage(10);
        copy.receiveDamage(5);

        //then
        assertEquals(45, pokemon.statistics().hp());
        assertEquals(40, copy.statistics().hp());
    }
}
