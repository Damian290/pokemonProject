package com.rest.pokemon.mapper;

import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.external.pokeapi.dto.details.StatisticsDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class PokemonDtoToPokemonMapper {

    public Pokemon map(PokemonDto pokemonDto, Integer id) {
        return new Pokemon(id, pokemonDto.name(), mapStatistics(pokemonDto.stats()));
    }

    private Statistics mapStatistics(List<StatisticsDto> statisticsDto) {
        return new Statistics(
                Math.max(1, getStatValue(statisticsDto, Stat.HP)),
                Math.max(1, getStatValue(statisticsDto, Stat.ATTACK)),
                getStatValue(statisticsDto, Stat.DEFENSE),
                getStatValue(statisticsDto, Stat.SPEED)
        );
    }

    private long getStatValue(List<StatisticsDto> statisticsDto, Stat stat) {
        return statisticsDto.stream()
                .filter(statisticDto -> Objects.equals(statisticDto.stat().name(), stat.getKey()))
                .map(StatisticsDto::baseStat)
                .findFirst()
                .orElse(0L);
    }
}
