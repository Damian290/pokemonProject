package com.rest.pokemon.feature.battle.duel;

import com.rest.pokemon.domain.battle.DuelResult;
import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.Statistics;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DuelResolverTest {

    private final DuelResolver sut = new DuelResolver();

    @Test
    void shouldReturnWinnerWithRemainingHpAndRestoredDefence() {
        //given
        var myPokemon = pokemon(1, "Pikachu", 100, 20, 10, 100);
        var opponent = pokemon(2, "Charmander", 40, 15, 5, 90);

        //when
        DuelResult result = sut.resolve(myPokemon, opponent);

        //then
        assertTrue(result.myPokemonWinner());
        assertThat(result.winner()).usingRecursiveComparison()
                .isEqualTo(pokemon(1, "Pikachu", 85, 20, 10, 100));
        assertEquals(2, result.defeated().id());
        assertEquals(0, result.defeated().statistics().hp());
        assertEquals(5, result.defeated().statistics().defence());
        assertEquals(100, myPokemon.statistics().hp());
        assertEquals(40, opponent.statistics().hp());
    }

    @Test
    void shouldConsumeDefenceBeforeReducingHp() {
        //given
        var myPokemon = pokemon(1, "Pikachu", 100, 20, 0, 100);
        var opponent = pokemon(2, "Charmander", 25, 1, 5, 90);

        //when
        DuelResult result = sut.resolve(myPokemon, opponent);

        //then
        assertEquals(0, result.defeated().statistics().hp());
        assertEquals(98, result.winner().statistics().hp());
    }

    @Test
    void shouldRejectDefeatedPokemonBeforeStartingDuel() {
        //given
        var myPokemon = pokemon(1, "Pikachu", 100, 20, 0, 100);
        myPokemon.receiveDamage(100);
        var opponent = pokemon(2, "Charmander", 40, 15, 0, 90);

        //when
        var thrown = assertThrows(IllegalStateException.class, () -> sut.resolve(myPokemon, opponent));

        //then
        assertNotNull(thrown.getMessage());
    }

    private Pokemon pokemon(int id, String name, long hp, long attack, long defence, long speed) {
        return new Pokemon(id, name, new Statistics(hp, attack, defence, speed));
    }
}
