package com.rest.pokemon.api;

import com.rest.pokemon.api.dto.PokemonDetailsResponseDto;
import com.rest.pokemon.api.dto.PokemonOverviewResponseDto;
import com.rest.pokemon.api.dto.PokemonTeamResponseDto;
import com.rest.pokemon.api.dto.StatisticsResponseDto;
import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.PokemonOverview;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PokemonApiMapper {
    public PokemonOverviewResponseDto toResponse(PokemonOverview pokemon) {
        return new PokemonOverviewResponseDto(pokemon.name(), pokemon.url());
    }

    public PokemonDetailsResponseDto toResponse(Pokemon pokemon) {
        var statistics = pokemon.statistics();
        return new PokemonDetailsResponseDto(pokemon.id(), pokemon.name(),
                new StatisticsResponseDto(statistics.hp(), statistics.attack(),
                        statistics.defence(), statistics.speed()));
    }

    public PokemonTeamResponseDto toResponse(PokemonTeam team) {
        return new PokemonTeamResponseDto(team.id(), team.name(),
                team.pokemons().stream().map(this::toResponse).toList());
    }

    public List<PokemonOverviewResponseDto> toOverviewResponses(List<PokemonOverview> pokemon) {
        return pokemon.stream().map(this::toResponse).toList();
    }

    public List<PokemonTeamResponseDto> toTeamResponses(List<PokemonTeam> teams) {
        return teams.stream().map(this::toResponse).toList();
    }
}
