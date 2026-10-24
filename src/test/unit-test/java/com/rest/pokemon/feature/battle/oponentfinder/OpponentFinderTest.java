package com.rest.pokemon.feature.battle.oponentfinder;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticsDto;
import com.rest.pokemon.mapper.PokemonDtoToPokemonDetailsMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpponentFinderTest {
    private final PokeApiConnector pokeApiConnector = mock(PokeApiConnector.class);
    private final Random random = mock(Random.class);
    private final PokemonDtoToPokemonDetailsMapper pokemonMapper = new PokemonDtoToPokemonDetailsMapper();
    private final OpponentFinder sut = new OpponentFinder(pokeApiConnector, pokemonMapper, random);

    @Test
    void shouldFetchAndMapSelectedOpponent() {
        //given
        int opponentId = 25;
        var pokemonDto = pokemonDto("Pikachu");
        when(random.nextInt(1, 152)).thenReturn(opponentId);
        when(pokeApiConnector.getPokemonDetails(opponentId)).thenReturn(pokemonDto);

        //when
        var result = sut.find(List.of());

        //then
        assertEquals(new PokemonDetails(opponentId, "Pikachu", new Statistics(50, 20, 10, 15)), result);
        verify(pokeApiConnector).getPokemonDetails(opponentId);
    }

    @Test
    void shouldReselectWhenRandomIdBelongsToMyTeam() {
        //given
        var teamPokemon = new PokemonDetails(25, "Pikachu", new Statistics(50, 20, 10, 15));
        int opponentId = 50;
        var opponentDto = pokemonDto("Diglett");
        when(random.nextInt(1, 152)).thenReturn(25, opponentId);
        when(pokeApiConnector.getPokemonDetails(opponentId)).thenReturn(opponentDto);

        //when
        var result = sut.find(List.of(teamPokemon));

        //then
        assertEquals(opponentId, result.id());
        verify(random, times(2)).nextInt(1, 152);
        verify(pokeApiConnector).getPokemonDetails(opponentId);
        verify(pokeApiConnector, never()).getPokemonDetails(25);
    }

    private PokemonDto pokemonDto(String name) {
        return new PokemonDto(name, List.of(
                new StatisticsDto(50, new StatisticDto("hp")),
                new StatisticsDto(20, new StatisticDto("attack")),
                new StatisticsDto(10, new StatisticDto("defense")),
                new StatisticsDto(15, new StatisticDto("speed"))));
    }
}
