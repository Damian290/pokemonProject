package com.rest.pokemon.repository;

import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.repository.exception.PokemonTeamNameAlreadyExistsException;
import com.rest.pokemon.repository.exception.PokemonTeamNotFoundException;
import com.rest.pokemon.repository.mapper.PokemonEntityMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PokemonTeamDaoImplTest {

    private final PokemonTeamDaoImpl sut = new PokemonTeamDaoImpl(new PokemonEntityMapper());
    private final Statistics testStats = new Statistics(100, 90, 10, 100);

    @Test
    void shouldCreateTeamAndReturnIsolatedDomainObject() {
        //given
        var pikachu = new Pokemon(25, "Pikachu", testStats);
        var charizard = new Pokemon(6, "Charizard", testStats);
        var expectedTeam = new PokemonTeam(1, "starter", List.of(
                new Pokemon(25, "Pikachu", testStats), new Pokemon(6, "Charizard", testStats)));

        //when
        var createdTeam = sut.create(new PokemonTeam(null, "starter", List.of(pikachu, charizard)));
        pikachu.receiveDamage(10);
        pikachu.receiveDamage(1);
        createdTeam.pokemons().getFirst().receiveDamage(10);
        createdTeam.pokemons().getFirst().receiveDamage(1);

        //then
        assertThat(sut.getById(1)).usingRecursiveComparison().isEqualTo(expectedTeam);
    }

    @Test
    void shouldReturnAllPokemonTeams() {
        //given
        var pokemon = new Pokemon(1, "Bulbasaur", testStats);
        var expectedTeam = new PokemonTeam(1, "test", List.of(pokemon));
        sut.create(new PokemonTeam(null, "test", List.of(pokemon)));

        //when
        var result = sut.getAll();

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(expectedTeam));
    }

    @Test
    void shouldStoreMultipleTeamsWithUniqueIdsAndNames() {
        //given
        var firstPokemon = new Pokemon(25, "Pikachu", testStats);
        var first = sut.create(new PokemonTeam(null, "Ash", List.of(firstPokemon)));

        //when
        var secondPokemon = new Pokemon(7, "Squirtle", testStats);
        var second = sut.create(new PokemonTeam(null, "Misty", List.of(secondPokemon)));

        //then
        assertThat(first).usingRecursiveComparison()
                .isEqualTo(new PokemonTeam(1, "Ash", List.of(firstPokemon)));
        assertThat(second).usingRecursiveComparison()
                .isEqualTo(new PokemonTeam(2, "Misty", List.of(secondPokemon)));
        assertThat(sut.getAll()).usingRecursiveComparison().isEqualTo(List.of(first, second));
    }

    @Test
    void shouldRejectDuplicateTeamName() {
        //given
        sut.create(new PokemonTeam(null, "Ash", List.of()));

        //when
        var thrown = assertThrows(PokemonTeamNameAlreadyExistsException.class,
                () -> sut.create(new PokemonTeam(null, "Ash", List.of())));

        //then
        assertEquals("Pokemon team name already exists: Ash", thrown.getMessage());
    }

    @Test
    void shouldReportMissingTeam() {
        //given
        var missingTeamName = "Missing";

        //when
        var byName = assertThrows(PokemonTeamNotFoundException.class,
                () -> sut.getByName(missingTeamName));
        var byId = assertThrows(PokemonTeamNotFoundException.class, () -> sut.getById(999));

        //then
        assertEquals("Pokemon team not found: Missing", byName.getMessage());
        assertEquals("Pokemon team not found: 999", byId.getMessage());
    }

    @Test
    void shouldSaveUpdatedTeamWithoutKeepingInputReference() {
        //given
        var firstPokemon = new Pokemon(16, "Pidgey", testStats);
        var secondPokemon = new Pokemon(19, "Rattata", testStats);
        var team = sut.create(new PokemonTeam(null, "test", List.of(firstPokemon, secondPokemon)));
        var updatedPokemon = new Pokemon(16, "Pidgey", new Statistics(10, 20, 30, 40));
        var updatedTeam = team.updatePokemonState(updatedPokemon);
        var expectedTeam = new PokemonTeam(1, "test", List.of(
                new Pokemon(16, "Pidgey", new Statistics(10, 20, 30, 40)), secondPokemon));

        //when
        sut.save(updatedTeam);
        updatedPokemon.receiveDamage(30);
        updatedPokemon.receiveDamage(1);

        //then
        assertThat(sut.getById(1)).usingRecursiveComparison().isEqualTo(expectedTeam);
    }

    @Test
    void shouldSaveTeamAfterRemovingDefeatedPokemon() {
        //given
        var firstPokemon = new Pokemon(133, "Eevee", testStats);
        var secondPokemon = new Pokemon(143, "Snorlax", testStats);
        var team = sut.create(new PokemonTeam(null, "test", List.of(firstPokemon, secondPokemon)));

        //when
        sut.save(team.removePokemon(firstPokemon));

        //then
        assertThat(sut.getById(1)).usingRecursiveComparison()
                .isEqualTo(new PokemonTeam(1, "test", List.of(secondPokemon)));
    }
}
