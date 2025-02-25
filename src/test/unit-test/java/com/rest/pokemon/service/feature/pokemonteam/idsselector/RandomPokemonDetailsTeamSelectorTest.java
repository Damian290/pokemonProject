package com.rest.pokemon.service.feature.pokemonteam.idsselector;

import com.rest.pokemon.service.feature.pokemonteam.idselector.RandomPokemonIdsSelector;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RandomPokemonDetailsTeamSelectorTest {

    private final RandomPokemonIdsSelector sut = new RandomPokemonIdsSelector(new Random());

    @Test
    void shouldSelectSixUniqueIds() {
        //given
        //when
        List<Integer> selectedPokemon = sut.selectSixIds();

        //then
        assertNotNull(selectedPokemon, "Pokemon list should not be null");
        assertEquals(6, selectedPokemon.size(), "Pokemon list size should be 6");
        Set<Integer> uniquePokemon = new HashSet<>(selectedPokemon);
        assertEquals(6, uniquePokemon.size(), "All Pokemons should be unique");
    }

    @Test
    void shouldSelectPokemonWithinValidRange() {
        //given
        //when
        List<Integer> selectedPokemon = sut.selectSixIds();

        //then
        assertTrue(selectedPokemon.stream().allMatch(id -> id >= 1 && id <= 151),
                "All ids should be in range 1-151");
    }
}
