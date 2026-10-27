package com.rest.pokemon.domain.battle;

import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.Statistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class DuelRingTest {

    @Test
    void shouldLetFasterPokemonAttackFirst() {
        //given
        var myPokemon = pokemon(1, 100);
        var opponent = pokemon(2, 80);

        //when
        var duelRing = new DuelRing(myPokemon, opponent);
        while (!duelRing.isDuelFinished()) {
            duelRing.performTurn();
        }

        //then
        assertEquals(myPokemon.id(), duelRing.getWinner().id());
    }

    @Test
    void shouldChooseMyPokemonFirstWhenSpeedsAreEqual() {
        //given
        var myPokemon = pokemon(1, 100);
        var opponent = pokemon(2, 100);

        //when
        var duelRing = new DuelRing(myPokemon, opponent);
        while (!duelRing.isDuelFinished()) {
            duelRing.performTurn();
        }

        //then
        assertEquals(myPokemon.id(), duelRing.getWinner().id());
    }

    @Test
    void shouldAlternateTurns() {
        //given
        var duelRing = new DuelRing(pokemon(1, 100), pokemon(2, 80));

        //when
        while (!duelRing.isDuelFinished()) {
            duelRing.performTurn();
        }

        //then
        assertEquals(10, duelRing.getWinner().statistics().hp());
        assertEquals(0, duelRing.getDefeated().statistics().hp());
    }

    @Test
    void shouldNotExposeWinnerBeforeDuelEnds() {
        //given
        var duelRing = new DuelRing(pokemon(1, 100), pokemon(2, 80));

        //when
        var thrown = assertThrows(IllegalStateException.class, duelRing::getWinner);

        //then
        assertEquals("Duel is not finished yet", thrown.getMessage());
        assertFalse(duelRing.isDuelFinished());
    }

    @ParameterizedTest
    @CsvSource({"true, 100, 80", "false, 80, 100"})
    void shouldIdentifyWinner(boolean myPokemonWins, long mySpeed, long opponentSpeed) {
        //given
        var duelRing = new DuelRing(pokemon(1, mySpeed), pokemon(2, opponentSpeed));

        //when
        while (!duelRing.isDuelFinished()) {
            duelRing.performTurn();
        }

        //then
        assertTrue(duelRing.isDuelFinished());
        assertEquals(myPokemonWins, duelRing.isMyPokemonWinner());
        assertEquals(myPokemonWins ? 1 : 2, duelRing.getWinner().id());
        assertEquals(myPokemonWins ? 2 : 1, duelRing.getDefeated().id());
    }

    @Test
    void shouldRejectDefeatedPokemonAtDuelStart() {
        //given
        var defeated = pokemon(1, 100);
        defeated.receiveDamage(50);

        //when
        var thrown = assertThrows(IllegalStateException.class,
                () -> new DuelRing(defeated, pokemon(2, 80)));

        //then
        assertEquals("A Pokemon with zero HP cannot enter a duel", thrown.getMessage());
    }

    @Test
    void shouldNotExposeMutableWinnerState() {
        //given
        var duelRing = new DuelRing(pokemon(1, 100), pokemon(2, 80));
        while (!duelRing.isDuelFinished()) {
            duelRing.performTurn();
        }

        //when
        duelRing.getWinner().receiveDamage(10);

        //then
        assertEquals(10, duelRing.getWinner().statistics().hp());
    }

    private Pokemon pokemon(int id, long speed) {
        return new Pokemon(id, "Pokemon" + id, new Statistics(50, 20, 0, speed));
    }
}
