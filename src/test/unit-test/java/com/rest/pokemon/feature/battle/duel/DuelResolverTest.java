package com.rest.pokemon.feature.battle.duel;

import com.rest.pokemon.domain.battle.DuelResult;
import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuelResolverTest {

    private final DuelResolver sut = new DuelResolver();

    @Test
    void shouldReturnDuelResultWithBothPokemonWithUpdatedHpAndBaseDefence() {
        //given
        var myPokemon = pokemon(1, "Pikachu", 100, 20, 10, 100);
        var opponent = pokemon(2, "Charmander", 40, 15, 5, 90);

        //when
        DuelResult result = sut.resolve(myPokemon, opponent);

        //then
        assertTrue(result.myPokemonWinner());
        assertSame(myPokemon, result.winner());
        assertEquals(85, myPokemon.statistics().getHp());
        assertEquals(10, myPokemon.statistics().getDefence());
        assertEquals(0, opponent.statistics().getHp());
        assertEquals(5, opponent.statistics().getDefence());
    }

    @Test
    void shouldConsumeDefenceBeforeReducingHp() {
        //given
        var myPokemon = pokemon(1, "Pikachu", 100, 20, 0, 100);
        var opponent = pokemon(2, "Charmander", 25, 1, 5, 90);

        //when
        sut.resolve(myPokemon, opponent);

        //then
        assertEquals(0, opponent.statistics().getHp());
        assertEquals(98, myPokemon.statistics().getHp());
    }

    @Test
    void shouldRejectPokemonWithNonPositiveHpBeforeStartingDuel() {
        //given
        var myPokemon = pokemon(1, "Pikachu", 0, 20, 0, 100);
        var opponent = pokemon(2, "Charmander", 40, 15, 0, 90);

        //when
        var thrown = assertThrows(IllegalStateException.class, () -> sut.resolve(myPokemon, opponent));

        //then
        assertNotNull(thrown.getMessage());
    }

    @Test
    void shouldRejectPokemonWithNonPositiveAttackBeforeStartingDuel() {
        //given
        var myPokemon = pokemon(1, "Pikachu", 100, 0, 0, 100);
        var opponent = pokemon(2, "Charmander", 40, 15, 0, 90);

        //when
        var thrown = assertThrows(IllegalStateException.class, () -> sut.resolve(myPokemon, opponent));

        //then
        assertNotNull(thrown.getMessage());
    }

    private PokemonDetails pokemon(int id, String name, long hp, long attack, long defence, long speed) {
        return new PokemonDetails(id, name, new Statistics(hp, attack, defence, speed));
    }
}
