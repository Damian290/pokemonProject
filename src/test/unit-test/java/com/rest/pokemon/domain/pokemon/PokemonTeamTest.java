package com.rest.pokemon.domain.pokemon;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PokemonTeamTest {

    @Test
    void shouldRejectNonPositiveTeamId() {
        //given
        var invalidId = 0;

        //when
        var thrown = assertThrows(IllegalArgumentException.class,
                () -> new PokemonTeam(invalidId, "Ash", List.of()));

        //then
        assertEquals("Team ID must be positive", thrown.getMessage());
    }

    @Test
    void shouldRejectBlankTeamName() {
        //given
        var blankName = "   ";

        //when
        var thrown = assertThrows(IllegalArgumentException.class,
                () -> new PokemonTeam(1, blankName, List.of()));

        //then
        assertEquals("Team name must not be blank", thrown.getMessage());
    }

    @Test
    void shouldRejectNullTeamName() {
        //given
        String nullName = null;

        //when
        var thrown = assertThrows(IllegalArgumentException.class,
                () -> new PokemonTeam(1, nullName, List.of()));

        //then
        assertEquals("Team name must not be blank", thrown.getMessage());
    }

    @Test
    void shouldRejectDuplicatePokemonIdsEvenWhenOtherFieldsDiffer() {
        //given
        var first = new Pokemon(25, "Pikachu", new Statistics(35, 55, 40, 90));
        var second = new Pokemon(25, "Another Pikachu", new Statistics(50, 20, 10, 30));

        //when
        var thrown = assertThrows(IllegalArgumentException.class,
                () -> new PokemonTeam(1, "Ash", List.of(first, second)));

        //then
        assertEquals("Duplicate Pokemon ID in team: 25", thrown.getMessage());
    }

    @Test
    void shouldAcceptPokemonWithDistinctIds() {
        //given
        var first = new Pokemon(25, "Pikachu", new Statistics(35, 55, 40, 90));
        var second = new Pokemon(4, "Charmander", new Statistics(39, 52, 43, 65));

        //when
        var team = new PokemonTeam(1, "Ash", List.of(first, second));

        //then
        assertThat(team.pokemons()).usingRecursiveComparison().isEqualTo(List.of(first, second));
    }
}
