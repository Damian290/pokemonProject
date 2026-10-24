package com.rest.pokemon.domain.battle;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class DuelRingTest {

    @Test
    void shouldChoosePokemonWithGreaterSpeedToAttackFirst() {
        //given
        var myPokemon = pokemon(1, "Pikachu", 50, 20, 10, 100);
        var opponent = pokemon(2, "Charmander", 50, 20, 10, 80);

        //when
        var duelRing = new DuelRing(myPokemon, opponent);

        //then
        assertSame(myPokemon, duelRing.getCurrentTurn().getPokemonDetails());
    }

    @Test
    void shouldChooseMyPokemonFirstWhenSpeedsAreEqual() {
        //given
        var myPokemon = pokemon(1, "Pikachu", 50, 20, 10, 100);
        var opponent = pokemon(2, "Charmander", 50, 20, 10, 100);

        //when
        var duelRing = new DuelRing(myPokemon, opponent);

        //then
        assertSame(myPokemon, duelRing.getCurrentTurn().getPokemonDetails());
    }

    @Test
    void shouldReturnOtherPokemonAsDamageReceiverWhenMyPokemonSpeedIsHigher() {
        //given
        var myPokemon = pokemon(1, "Pikachu", 50, 20, 10, 100);
        var opponent = pokemon(2, "Charmander", 50, 20, 10, 80);
        var duelRing = new DuelRing(myPokemon, opponent);

        //when
        var damageReceiver = duelRing.damageReceiver();

        //then
        assertSame(opponent, damageReceiver.getPokemonDetails());
    }

    @Test
    void shouldNotHaveWinnerWhileBothPokemonAreAlive() {
        //given
        var duelRing = new DuelRing(
                pokemon(1, "Pikachu", 50, 20, 10, 100),
                pokemon(2, "Charmander", 50, 20, 10, 80));

        //when
        var winner = duelRing.getWinner();

        //then
        assertNull(winner);
        assertFalse(duelRing.isDuelFinished());
    }

    @ParameterizedTest
    @CsvSource({"false", "true"})
    void shouldIdentifyWhetherMyPokemonIsWinner(boolean myPokemonWins) {
        //given
        var myPokemon = pokemon(1, "Pikachu", 50, 20, 10, 100);
        var opponent = pokemon(2, "Charmander", 50, 20, 10, 80);
        var duelRing = new DuelRing(myPokemon, opponent);
        if (myPokemonWins) {
            duelRing.getOpponent().getPokemonDetails().statistics().setHp(0);
        } else {
            duelRing.getMyPokemon().getPokemonDetails().statistics().setHp(0);
        }

        //when
        var result = duelRing.isMyPokemonWinner();

        //then
        assertEquals(myPokemonWins, result);
    }

    @Test
    void shouldRejectPokemonWithNonPositiveHp() {
        //given
        var myPokemon = pokemon(1, "Pikachu", 0, 20, 10, 100);
        var opponent = pokemon(2, "Charmander", 50, 20, 10, 80);

        //when
        var thrown = assertThrows(IllegalStateException.class, () -> new DuelRing(myPokemon, opponent));

        //then
        assertNotNull(thrown.getMessage());
    }

    @Test
    void shouldRejectPokemonWithNonPositiveAttack() {
        //given
        var myPokemon = pokemon(1, "Pikachu", 50, 0, 10, 100);
        var opponent = pokemon(2, "Charmander", 50, 20, 10, 80);

        //when
        var thrown = assertThrows(IllegalStateException.class, () -> new DuelRing(myPokemon, opponent));

        //then
        assertNotNull(thrown.getMessage());
    }

    @Test
    void shouldRejectPokemonWithoutStatistics() {
        //given
        var myPokemon = new PokemonDetails(1, "Pikachu", null);
        var opponent = pokemon(2, "Charmander", 50, 20, 10, 80);

        //when
        var thrown = assertThrows(IllegalStateException.class, () -> new DuelRing(myPokemon, opponent));

        //then
        assertNotNull(thrown.getMessage());
    }

    private PokemonDetails pokemon(int id, String name, long hp, long attack, long defence, long speed) {
        return new PokemonDetails(id, name, new Statistics(hp, attack, defence, speed));
    }
}
