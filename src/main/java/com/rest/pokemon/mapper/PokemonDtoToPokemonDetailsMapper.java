package com.rest.pokemon.mapper;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticsDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class PokemonDtoToPokemonDetailsMapper {

    public PokemonDetails map(PokemonDto pokemonDto, Integer id) {
        return PokemonDetails.builder()
                .id(id)
                .name(pokemonDto.name())
                .statistics(mapStatistics(pokemonDto.stats()))
                .build();
    }

    private Statistics mapStatistics(List<StatisticsDto> statisticsDto) {
        return Statistics.builder()
                .hp(Math.max(1, getStatValue(statisticsDto, Stat.HP)))
                .attack(Math.max(1, getStatValue(statisticsDto, Stat.ATTACK)))
                .defence(getStatValue(statisticsDto, Stat.DEFENSE))
                .speed(getStatValue(statisticsDto, Stat.SPEED))
                .build();
    }

    private long getStatValue(List<StatisticsDto> statisticsDto, Stat stat) {
        return statisticsDto.stream()
                .filter(statisticDto -> Objects.equals(statisticDto.stat().name(), stat.getKey()))
                .map(StatisticsDto::baseStat)
                .findFirst()
                .orElse(0L);
    }
}
