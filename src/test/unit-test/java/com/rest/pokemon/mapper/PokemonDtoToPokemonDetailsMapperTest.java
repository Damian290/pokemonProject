package com.rest.pokemon.mapper;

import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticsDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PokemonDtoToPokemonDetailsMapperTest {
    private final PokemonDtoToPokemonDetailsMapper sut = new PokemonDtoToPokemonDetailsMapper();

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
        assertEquals(35, pokemon.statistics().getHp());
        assertEquals(55, pokemon.statistics().getAttack());
        assertEquals(40, pokemon.statistics().getDefence());
        assertEquals(90, pokemon.statistics().getSpeed());
    }

    @Test
    void shouldSetMinimumHpAndAttackWhenExternalStatsAreMissingOrZero() {
        //given
        var dto = new PokemonDto("Missingno", List.of());

        //when
        var pokemon = sut.map(dto, 0);

        //then
        assertEquals(1, pokemon.statistics().getHp());
        assertEquals(1, pokemon.statistics().getAttack());
        assertEquals(0, pokemon.statistics().getDefence());
        assertEquals(0, pokemon.statistics().getSpeed());
    }
}
