package com.rest.pokemon.service.feature.battle.onevsone;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OneVsOneRingTest {

    @Test
    void shouldDetermineCurrentTurnForTheFirstBasedOnSpeed() {
        // Given
        PokemonDetails fasterPokemon = createPokemon(1, "Pikachu", 50, 20, 10, 100);
        PokemonDetails slowerPokemon = createPokemon(2, "Charmander", 50, 20, 10, 80);

        // When
        OneVsOneRing battleRing = new OneVsOneRing(fasterPokemon, slowerPokemon);
        var currentTurn = battleRing.getCurrentTurn();

        // Then
        assertThat(currentTurn.getPokemonDetails()).isEqualTo(fasterPokemon);
    }

    @Test
    void shouldDetermineDamageReceiverCorrectly() {
        // Given
        PokemonDetails myPokemon = createPokemon(1, "Pikachu", 50, 20, 10, 100);
        PokemonDetails opponent = createPokemon(2, "Charmander", 50, 20, 10, 80);

        // When
        OneVsOneRing battleRing = new OneVsOneRing(myPokemon, opponent);
        PokemonInDuel damageReceiver = battleRing.damageReceiver();

        // Then
        assertThat(damageReceiver.getPokemonDetails()).isEqualTo(opponent);
    }

    @Test
    void shouldReturnWinnerWhenMyPokemonHpIsZero() {
        // Given
        PokemonDetails myPokemon = createPokemon(1, "Pikachu", 0, 20, 10, 100);
        PokemonDetails opponent = createPokemon(2, "Charmander", 50, 20, 10, 80);

        // When
        OneVsOneRing battleRing = new OneVsOneRing(myPokemon, opponent);
        PokemonInDuel winner = battleRing.getWinner();

        // Then
        assertThat(winner.getPokemonDetails()).isEqualTo(opponent);
    }

    @Test
    void shouldReturnWinnerWhenOpponentHpIsZero() {
        // Given
        PokemonDetails myPokemon = createPokemon(1, "Pikachu", 50, 20, 10, 100);
        PokemonDetails opponent = createPokemon(2, "Charmander", 0, 20, 10, 80);
        OneVsOneRing battleRing = new OneVsOneRing(myPokemon, opponent);

        // When
        PokemonInDuel winner = battleRing.getWinner();

        // Then
        assertThat(winner.getPokemonDetails()).isEqualTo(myPokemon);
    }

    @Test
    void shouldNotDetermineWinnerWhenBothPokemonHaveHpAboveZero() {
        // Given
        PokemonDetails myPokemon = createPokemon(1, "Pikachu", 50, 20, 10, 100);
        PokemonDetails opponent = createPokemon(2, "Charmander", 50, 20, 10, 80);
        OneVsOneRing battleRing = new OneVsOneRing(myPokemon, opponent);

        // When
        PokemonInDuel winner = battleRing.getWinner();

        // Then
        assertThat(winner).isNull();
    }

    @Test
    void shouldCorrectlyDetermineIfMyPokemonIsWinner() {
        // Given
        PokemonDetails myPokemon = createPokemon(1, "Pikachu", 50, 20, 10, 100);
        PokemonDetails opponent = createPokemon(2, "Charmander", 0, 20, 10, 80);
        OneVsOneRing battleRing = new OneVsOneRing(myPokemon, opponent);

        // When
        boolean isMyPokemonWinner = battleRing.isMyPokemonWinner();

        // Then
        assertThat(isMyPokemonWinner).isTrue();
    }

    private PokemonDetails createPokemon(int id, String name, long hp, long attack, long defence, long speed) {
        Statistics stats = new Statistics(hp, attack, defence, speed);
        return new PokemonDetails(id, name, stats);
    }
}
