package com.rest.pokemon.api;

import com.rest.pokemon.api.dto.PokemonDetailsResponseDto;
import com.rest.pokemon.api.dto.PokemonOverviewResponseDto;
import com.rest.pokemon.api.dto.PokemonTeamResponseDto;
import com.rest.pokemon.api.dto.StatisticsResponseDto;
import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.PokemonOverview;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.domain.pokemon.Statistics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PokemonApiMapperTest {
    private final PokemonApiMapper sut = new PokemonApiMapper();

    @Test
    void shouldMapPokemonOverviewToResponseDto() {
        //given
        var pokemon = new PokemonOverview("Pikachu", "/pokemon/25");

        //when
        var response = sut.toResponse(pokemon);

        //then
        assertEquals(new PokemonOverviewResponseDto("Pikachu", "/pokemon/25"), response);
    }

    @Test
    void shouldMapPokemonDetailsToResponseDto() {
        //given
        var pokemon = new PokemonDetails(25, "Pikachu", new Statistics(35, 55, 40, 90));

        //when
        var response = sut.toResponse(pokemon);

        //then
        assertEquals(new PokemonDetailsResponseDto(25, "Pikachu",
                new StatisticsResponseDto(35, 55, 40, 90)), response);
    }

    @Test
    void shouldMapPokemonTeamAndItsMembersToResponseDto() {
        //given
        var member = new PokemonDetails(25, "Pikachu", new Statistics(35, 55, 40, 90));
        var team = new PokemonTeam(3, "Ash", List.of(member));

        //when
        var response = sut.toResponse(team);

        //then
        assertEquals(new PokemonTeamResponseDto(3, "Ash", List.of(
                new PokemonDetailsResponseDto(25, "Pikachu",
                        new StatisticsResponseDto(35, 55, 40, 90)))), response);
    }

    @Test
    void shouldMapPokemonOverviewListToResponseDtos() {
        //given
        var overview = new PokemonOverview("Pikachu", "/pokemon/25");

        //when
        var overviews = sut.toOverviewResponses(List.of(overview));

        //then
        assertEquals(List.of(new PokemonOverviewResponseDto("Pikachu", "/pokemon/25")), overviews);
    }

    @Test
    void shouldMapPokemonTeamListToResponseDtos() {
        //given
        var team = new PokemonTeam(1, "Ash", List.of());

        //when
        var teams = sut.toTeamResponses(List.of(team));

        //then
        assertEquals(List.of(new PokemonTeamResponseDto(1, "Ash", List.of())), teams);
    }
}
