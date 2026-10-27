package com.rest.pokemon.mapper;

import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticsDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PokemonDtoToPokemonMapperTest {
    private final PokemonDtoToPokemonMapper sut = new PokemonDtoToPokemonMapper();

    @Test
    void shouldMapPokemonFieldsAndStatistics() {
        //given
        var dto = new PokemonDto("Pikachu", List.of(
                new StatisticsDto(35, new StatisticDto("hp")),
                new StatisticsDto(55, new StatisticDto("attack")),
                new StatisticsDto(40, new StatisticDto("defense")),
                new StatisticsDto(90, new StatisticDto("speed"))));

        //when
        var pokemon = sut.map(dto, 25);

        //then
        assertEquals(25, pokemon.id());
        assertEquals("Pikachu", pokemon.name());
        assertEquals(35, pokemon.statistics().hp());
        assertEquals(55, pokemon.statistics().attack());
        assertEquals(40, pokemon.statistics().defence());
        assertEquals(90, pokemon.statistics().speed());
    }

    @Test
    void shouldSetMinimumHpAndAttackWhenExternalStatsAreMissingOrZero() {
        //given
        var dto = new PokemonDto("Missingno", List.of());

        //when
        var pokemon = sut.map(dto, 0);

        //then
        assertEquals(1, pokemon.statistics().hp());
        assertEquals(1, pokemon.statistics().attack());
        assertEquals(0, pokemon.statistics().defence());
        assertEquals(0, pokemon.statistics().speed());
    }
}
