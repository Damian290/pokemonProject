package com.rest.pokemon.repository;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokemonTeamDaoImplTest {

    private final PokemonTeamDaoImpl sut = new PokemonTeamDaoImpl(new PokemonTeam(new ArrayList<>()));

    Statistics testStats = Statistics.builder()
            .hp(100)
            .attack(90)
            .defence(10)
            .speed(100)
            .build();

    @Test
    void shouldCreatePokemonTeam() {
        // Given
        List<PokemonDetails> newPokemonList = List.of(
                new PokemonDetails(25, "Pikachu", testStats),
                new PokemonDetails(6, "Charizard", testStats)
        );

        // When
        PokemonTeam team = sut.create(newPokemonList);

        // Then
        assertEquals(2, team.pokemonDetails().size());
        assertEquals("Pikachu", team.pokemonDetails().get(0).name());
        assertEquals(testStats, team.pokemonDetails().get(0).statistics());
        assertEquals("Charizard", team.pokemonDetails().get(1).name());
        assertEquals(testStats, team.pokemonDetails().get(1).statistics());
    }

    @Test
    void shouldReturnAllPokemon() {
        // Given
        List<PokemonDetails> initialPokemonList = List.of(
                new PokemonDetails(1, "Bulbasaur", testStats),
                new PokemonDetails(7, "Squirtle", testStats)
        );
        sut.create(initialPokemonList);

        // When
        PokemonTeam result = sut.getAll();

        // Then
        assertEquals(2, result.pokemonDetails().size());
        assertEquals("Bulbasaur", result.pokemonDetails().get(0).name());
        assertEquals("Squirtle", result.pokemonDetails().get(1).name());
    }

    @Test
    void shouldRemoveFirstPokemon() {
        // Given
        List<PokemonDetails> initialPokemonList = List.of(
                new PokemonDetails(133, "Eevee", testStats),
                new PokemonDetails(143, "Snorlax", testStats)
        );
        sut.create(initialPokemonList);

        // When
        PokemonDetails removedPokemon = sut.removeFirst();

        // Then
        assertEquals("Eevee", removedPokemon.name());
        assertEquals(1, sut.getAll().pokemonDetails().size());
        assertEquals("Snorlax", sut.getAll().pokemonDetails().getFirst().name());
    }

    @Test
    void shouldUpdateFirstPokemon() {
        // Given
        List<PokemonDetails> initialPokemonList = List.of(
                new PokemonDetails(16, "Pidgey", testStats),
                new PokemonDetails(19, "Rattata", testStats)
        );
        sut.create(initialPokemonList);

        // When
        Statistics updatedStats = Statistics.builder()
                .hp(10)
                .attack(10)
                .defence(10)
                .speed(10)
                .build();
        PokemonDetails updatedPokemon = new PokemonDetails(22, "Fearow", updatedStats);
        sut.updateFirstPokemon(updatedPokemon);

        // Then
        assertEquals("Fearow", sut.getAll().pokemonDetails().getFirst().name());
        assertEquals(updatedStats, sut.getAll().pokemonDetails().getFirst().statistics());
        assertEquals(2, sut.getAll().pokemonDetails().size());
    }
}
