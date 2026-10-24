package com.rest.pokemon.feature.pokemonteam;

import com.rest.pokemon.concurrent.VirtualThreadTaskRunner;
import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticsDto;
import com.rest.pokemon.feature.pokemonteam.idselector.PokemonIdsSelector;
import com.rest.pokemon.mapper.PokemonDtoToPokemonDetailsMapper;
import com.rest.pokemon.repository.PokemonTeamDao;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class PokemonTeamCreatorTest {
    private final PokemonIdsSelector pokemonIdsSelector = mock(PokemonIdsSelector.class);
    private final PokeApiConnector pokeApiConnector = mock(PokeApiConnector.class);
    private final PokemonTeamDao pokemonTeamDao = mock(PokemonTeamDao.class);
    private final VirtualThreadTaskRunner virtualThreadTaskRunner = new VirtualThreadTaskRunner();
    private final PokemonDtoToPokemonDetailsMapper pokemonMapper = new PokemonDtoToPokemonDetailsMapper();
    private final PokemonTeamCreator sut = new PokemonTeamCreator(
            pokemonIdsSelector, pokeApiConnector, pokemonMapper, pokemonTeamDao, virtualThreadTaskRunner);

    @Test
    void shouldFetchMapAndSaveSelectedPokemon() {
        //given
        var pokemonIds = List.of(1, 2, 3, 4, 5, 6);
        when(pokemonIdsSelector.selectSixIds()).thenReturn(pokemonIds);
        when(pokeApiConnector.getPokemonDetails(anyInt()))
                .thenAnswer(invocation -> pokemonDto("Pokemon", 50, 20, 10, 15));
        var expectedDetails = pokemonIds.stream()
                .map(id -> pokemon(id, "Pokemon", 50, 20, 10, 15))
                .toList();
        var expectedTeam = new PokemonTeam(1, "test", expectedDetails);
        when(pokemonTeamDao.create("test", expectedDetails)).thenReturn(expectedTeam);

        //when
        var result = sut.create("test");

        //then
        assertEquals(expectedTeam, result);
        verify(pokemonIdsSelector).selectSixIds();
        pokemonIds.forEach(id -> verify(pokeApiConnector).getPokemonDetails(id));
        verify(pokemonTeamDao).create("test", expectedDetails);
    }

    @Test
    void shouldNotSaveTeamWhenAnyDetailsRequestFails() {
        //given
        when(pokemonIdsSelector.selectSixIds()).thenReturn(List.of(1, 2));
        when(pokeApiConnector.getPokemonDetails(1)).thenReturn(pokemonDto("Pokemon", 50, 20, 10, 15));
        var failure = new IllegalStateException("PokeAPI failed");
        when(pokeApiConnector.getPokemonDetails(2)).thenThrow(failure);

        //when
        var thrown = assertThrows(IllegalStateException.class, () -> sut.create("test"));

        //then
        assertSame(failure, thrown);
        verify(pokemonTeamDao, never()).create(eq("test"), anyList());
    }

    private PokemonDetails pokemon(int id, String name, long hp, long attack, long defence, long speed) {
        return new PokemonDetails(id, name, new Statistics(hp, attack, defence, speed));
    }

    private PokemonDto pokemonDto(String name, long hp, long attack, long defence, long speed) {
        return new PokemonDto(name, List.of(
                new StatisticsDto(hp, new StatisticDto("hp")),
                new StatisticsDto(attack, new StatisticDto("attack")),
                new StatisticsDto(defence, new StatisticDto("defense")),
                new StatisticsDto(speed, new StatisticDto("speed"))));
    }
}
