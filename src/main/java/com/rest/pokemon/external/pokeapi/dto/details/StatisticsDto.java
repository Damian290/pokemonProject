package com.rest.pokemon.external.pokeapi.dto.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record StatisticsDto(
        @JsonProperty("base_stat")
        long baseStat,
        StatisticDto stat
) {
}
