package com.rest.pokemon.repository;

import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.repository.entity.jpa.JpaPokemonEntity;
import com.rest.pokemon.repository.entity.jpa.JpaPokemonTeamEntity;
import com.rest.pokemon.repository.exception.PokemonTeamNameAlreadyExistsException;
import com.rest.pokemon.repository.exception.PokemonTeamNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JpaPokemonTeamDaoTest {

    private final JpaPokemonTeamRepository teamRepository = mock(JpaPokemonTeamRepository.class);
    private final JpaPokemonEntityMapper mapper = mock(JpaPokemonEntityMapper.class);
    private final JpaPokemonTeamDao sut = new JpaPokemonTeamDao(teamRepository, mapper);

    @Test
    void shouldCreateTeamFromDomainObject() {
        //given
        var pokemon = pokemon();
        var newTeam = new PokemonTeam(null, "Ash", List.of(pokemon));
        var savedTeam = new PokemonTeam(1, "Ash", List.of(pokemon));
        var pokemonEntity = new JpaPokemonEntity(25, "Pikachu", 35, 55, 40, 90);
        when(mapper.toEntity(any(Pokemon.class))).thenReturn(pokemonEntity);
        when(teamRepository.save(any(JpaPokemonTeamEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDomain(any(JpaPokemonTeamEntity.class))).thenReturn(savedTeam);

        //when
        var result = sut.create(newTeam);

        //then
        var entityCaptor = ArgumentCaptor.forClass(JpaPokemonTeamEntity.class);
        verify(teamRepository).save(entityCaptor.capture());
        assertEquals("Ash", entityCaptor.getValue().getName());
        assertEquals(List.of(pokemonEntity), entityCaptor.getValue().getPokemons());
        assertSame(savedTeam, result);
    }

    @Test
    void shouldRejectCreatingTeamWithAssignedId() {
        //given
        var existingTeam = new PokemonTeam(1, "Ash", List.of());

        //when
        var exception = assertThrows(IllegalArgumentException.class, () -> sut.create(existingTeam));

        //then
        assertEquals("New team must not have an ID", exception.getMessage());
        verifyNoInteractions(teamRepository, mapper);
    }

    @Test
    void shouldRejectDuplicateTeamName() {
        //given
        var newTeam = new PokemonTeam(null, "Ash", List.of());
        when(teamRepository.existsByName("Ash")).thenReturn(true);

        //when
        var exception = assertThrows(PokemonTeamNameAlreadyExistsException.class, () -> sut.create(newTeam));

        //then
        assertEquals("Pokemon team name already exists: Ash", exception.getMessage());
        verify(teamRepository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldReturnAllTeamsInRepositoryOrder() {
        //given
        var firstEntity = new JpaPokemonTeamEntity("Ash");
        var secondEntity = new JpaPokemonTeamEntity("Misty");
        var firstTeam = new PokemonTeam(1, "Ash", List.of());
        var secondTeam = new PokemonTeam(2, "Misty", List.of());
        when(teamRepository.findAllByOrderByIdAsc()).thenReturn(List.of(firstEntity, secondEntity));
        when(mapper.toDomain(firstEntity)).thenReturn(firstTeam);
        when(mapper.toDomain(secondEntity)).thenReturn(secondTeam);

        //when
        var result = sut.getAll();

        //then
        assertEquals(List.of(firstTeam, secondTeam), result);
    }

    @Test
    void shouldReturnTeamById() {
        //given
        var entity = new JpaPokemonTeamEntity("Ash");
        var expectedTeam = new PokemonTeam(1, "Ash", List.of());
        when(teamRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(expectedTeam);

        //when
        var result = sut.getById(1);

        //then
        assertSame(expectedTeam, result);
    }

    @Test
    void shouldReportMissingTeamById() {
        //given
        var missingId = 99L;
        when(teamRepository.findById(missingId)).thenReturn(Optional.empty());

        //when
        var exception = assertThrows(PokemonTeamNotFoundException.class, () -> sut.getById(missingId));

        //then
        assertEquals("Pokemon team not found: 99", exception.getMessage());
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldReturnTeamByName() {
        //given
        var entity = new JpaPokemonTeamEntity("Ash");
        var expectedTeam = new PokemonTeam(1, "Ash", List.of());
        when(teamRepository.findByName("Ash")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(expectedTeam);

        //when
        var result = sut.getByName("Ash");

        //then
        assertSame(expectedTeam, result);
    }

    @Test
    void shouldReportMissingTeamByName() {
        //given
        var missingName = "Missing";
        when(teamRepository.findByName(missingName)).thenReturn(Optional.empty());

        //when
        var exception = assertThrows(PokemonTeamNotFoundException.class, () -> sut.getByName(missingName));

        //then
        assertEquals("Pokemon team not found: Missing", exception.getMessage());
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldSaveUpdatedTeamState() {
        //given
        var team = new PokemonTeam(1, "Ash", List.of(pokemon()));
        var storedEntity = new JpaPokemonTeamEntity("Old name");
        var pokemonEntity = new JpaPokemonEntity(25, "Pikachu", 35, 55, 40, 90);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(storedEntity));
        when(mapper.toEntity(any(Pokemon.class))).thenReturn(pokemonEntity);

        //when
        sut.save(team);

        //then
        assertEquals("Ash", storedEntity.getName());
        assertEquals(List.of(pokemonEntity), storedEntity.getPokemons());
        verify(teamRepository).save(storedEntity);
    }

    @Test
    void shouldRejectSavingTeamWithoutId() {
        //given
        var newTeam = new PokemonTeam(null, "Ash", List.of());

        //when
        var exception = assertThrows(IllegalArgumentException.class, () -> sut.save(newTeam));

        //then
        assertEquals("Team ID must be assigned before saving", exception.getMessage());
        verifyNoInteractions(teamRepository, mapper);
    }

    private Pokemon pokemon() {
        return new Pokemon(25, "Pikachu", new Statistics(35, 55, 40, 90));
    }
}
