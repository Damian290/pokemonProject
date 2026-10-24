package com.rest.pokemon.repository;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.repository.mapper.PokemonDetailsEntityMapper;
import com.rest.pokemon.repository.exception.PokemonTeamNameAlreadyExistsException;
import com.rest.pokemon.repository.exception.PokemonTeamNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PokemonTeamDaoImplTest {

    private final PokemonTeamDaoImpl sut = new PokemonTeamDaoImpl(new PokemonDetailsEntityMapper());

    Statistics testStats = Statistics.builder()
            .hp(100)
            .attack(90)
            .defence(10)
            .speed(100)
            .build();

    @Test
    void shouldCreateTeamAndReturnIsolatedDomainObject() {
        //given
        List<PokemonDetails> newPokemonList = List.of(
                new PokemonDetails(25, "Pikachu", testStats),
                new PokemonDetails(6, "Charizard", testStats)
        );
        var expectedCreatedTeam = new PokemonTeam(1, "starter", List.of(
                new PokemonDetails(25, "Pikachu", testStats),
                new PokemonDetails(6, "Charizard", testStats)
        ));

        //when
        PokemonTeam team = sut.create("starter", newPokemonList);

        //then
        assertEquals(expectedCreatedTeam, team);

        //when
        team.pokemonDetails().getFirst().statistics().setHp(1);
        team.pokemonDetails().getFirst().statistics().setAttack(1);
        team.pokemonDetails().getFirst().statistics().setDefence(1);

        //then
        assertEquals(expectedCreatedTeam, sut.getById(1));
    }

    @Test
    void shouldReturnAllPokemon() {
        //given
        List<PokemonDetails> initialPokemonList = List.of(
                new PokemonDetails(1, "Bulbasaur", testStats),
                new PokemonDetails(7, "Squirtle", testStats)
        );
        var expectedTeam = new PokemonTeam(1, "test", initialPokemonList);
        sut.create("test", initialPokemonList);

        //when
        var result = sut.getAll();

        //then
        assertEquals(List.of(expectedTeam), result);
    }

    @Test
    void shouldStoreMultipleTeamsWithUniqueIdsAndNames() {
        //given
        var firstPokemon = new PokemonDetails(25, "Pikachu", testStats);
        var first = sut.create("Ash", List.of(firstPokemon));

        //when
        var secondPokemon = new PokemonDetails(7, "Squirtle", testStats);
        var second = sut.create("Misty", List.of(secondPokemon));

        //then
        assertEquals(new PokemonTeam(1, "Ash", List.of(firstPokemon)), first);
        assertEquals(new PokemonTeam(2, "Misty", List.of(secondPokemon)), second);
        assertEquals(List.of(first, second), sut.getAll());
    }

    @Test
    void shouldThrowPokemonTeamNameAlreadyExistsExceptionWhenTeamNameAlreadyExists() {
        //given
        sut.create("Ash", List.of());

        //when
        var thrown = assertThrows(PokemonTeamNameAlreadyExistsException.class,
                () -> sut.create("Ash", List.of()));

        //then
        assertEquals("Pokemon team name already exists: Ash", thrown.getMessage());
    }

    @Test
    void shouldThrowPokemonTeamNotFoundExceptionWhenTeamDoesNotExist() {
        //given
        var missingTeamName = "Missing";

        //when
        var byName = assertThrows(PokemonTeamNotFoundException.class, () -> sut.getByName(missingTeamName));
        var byId = assertThrows(PokemonTeamNotFoundException.class, () -> sut.getById(999));

        //then
        assertEquals("Pokemon team not found: Missing", byName.getMessage());
        assertEquals("Pokemon team not found: 999", byId.getMessage());
    }

    @Test
    void shouldRemoveFirstPokemon() {
        //given
        List<PokemonDetails> initialPokemonList = List.of(
                new PokemonDetails(133, "Eevee", testStats),
                new PokemonDetails(143, "Snorlax", testStats)
        );
        sut.create("test", initialPokemonList);
        var expectedRemovedPokemon = initialPokemonList.getFirst();
        var expectedRemainingTeam = new PokemonTeam(1, "test", List.of(initialPokemonList.get(1)));

        //when
        PokemonDetails removedPokemon = sut.removeFirst(1);

        //then
        assertEquals(expectedRemovedPokemon, removedPokemon);
        assertEquals(expectedRemainingTeam, sut.getById(1));
    }

    @Test
    void shouldStoreUpdateAsEntityInsteadOfKeepingInputReference() {
        //given
        var originalPokemon = new PokemonDetails(16, "Pidgey", testStats);
        var secondPokemon = new PokemonDetails(19, "Rattata", testStats);
        sut.create("test", List.of(originalPokemon, secondPokemon));
        var updatedStats = new Statistics(10, 20, 30, 40);
        var updatedPokemon = new PokemonDetails(22, "Fearow", updatedStats);

        //when
        sut.updateFirstPokemon(1, updatedPokemon);
        updatedStats.setHp(1);

        //then
        var expectedStoredTeam = new PokemonTeam(1, "test", List.of(
                new PokemonDetails(22, "Fearow", new Statistics(10, 20, 30, 40)),
                secondPokemon
        ));
        assertEquals(expectedStoredTeam, sut.getById(1));
    }
}
