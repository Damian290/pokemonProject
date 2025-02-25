package com.rest.pokemon.service.feature.battle.onevsone;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PokemonInDuelTest {

    @Test
    void shouldUpdateDefenceLevel() {
        // Given
        PokemonDetails pokemonDetails = createPokemon(1, "Bulbasaur", 45, 49, 49, 45);
        PokemonInDuel pokemonInDuel = new PokemonInDuel(pokemonDetails, 49);

        // When
        pokemonInDuel.setCurrentDefenceLvl(30);

        // Then
        assertThat(pokemonInDuel.getCurrentDefenceLvl()).isEqualTo(30);
    }

    @Test
    void shouldNotSetNegativeDefenceLevel() {
        // Given
        PokemonDetails pokemonDetails = createPokemon(1, "Bulbasaur", 45, 49, 49, 45);
        PokemonInDuel pokemonInDuel = new PokemonInDuel(pokemonDetails, 49);

        // When
        pokemonInDuel.setCurrentDefenceLvl(-10);

        // Then
        assertThat(pokemonInDuel.getCurrentDefenceLvl()).isEqualTo(0);
    }

    private PokemonDetails createPokemon(int id, String name, long hp, long attack, long defence, long speed) {
        Statistics stats = new Statistics(hp, attack, defence, speed);
        return new PokemonDetails(id, name, stats);
    }
}
