package com.rest.pokemon.api;

import com.rest.pokemon.api.dto.PokemonDetailsResponseDto;
import com.rest.pokemon.api.dto.PokemonTeamResponseDto;
import com.rest.pokemon.api.dto.StatisticsResponseDto;
import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.feature.battle.BattleManager;
import com.rest.pokemon.feature.pokemonteam.PokemonTeamCreator;
import com.rest.pokemon.feature.pokemonteam.PokemonTeamService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PokemonTeamEndpointTest {
    private final PokemonTeamCreator pokemonTeamCreator = mock(PokemonTeamCreator.class);
    private final BattleManager battleManager = mock(BattleManager.class);
    private final PokemonTeamService pokemonTeamService = mock(PokemonTeamService.class);
    private final PokemonApiMapper pokemonApiMapper = mock(PokemonApiMapper.class);
    private final PokemonTeamEndpoint sut = new PokemonTeamEndpoint(
            pokemonTeamCreator, battleManager, pokemonTeamService, pokemonApiMapper);

    @Test
    void shouldUsePokemonTeamServiceAndMapperToGetTeams() {
        //given
        var teams = List.of(new PokemonTeam(1, "Ash", List.of()));
        var response = List.of(new PokemonTeamResponseDto(1, "Ash", List.of()));
        when(pokemonTeamService.getTeams()).thenReturn(teams);
        when(pokemonApiMapper.toTeamResponses(teams)).thenReturn(response);

        //when
        var result = sut.getTeams();

        //then
        assertEquals(response, result);
        verify(pokemonTeamService).getTeams();
        verify(pokemonApiMapper).toTeamResponses(teams);
    }

    @Test
    void shouldUsePokemonTeamCreatorAndMapperToCreateTeam() {
        //given
        var team = new PokemonTeam(1, "Ash", List.of());
        var response = new PokemonTeamResponseDto(1, "Ash", List.of());
        when(pokemonTeamCreator.create("Ash")).thenReturn(team);
        when(pokemonApiMapper.toResponse(team)).thenReturn(response);

        //when
        var result = sut.createTeam("Ash");

        //then
        assertEquals(response, result);
        verify(pokemonTeamCreator).create("Ash");
        verify(pokemonApiMapper).toResponse(team);
    }

    @Test
    void shouldUsePokemonTeamServiceAndMapperToGetTeamById() {
        //given
        var team = new PokemonTeam(1, "Ash", List.of());
        var response = new PokemonTeamResponseDto(1, "Ash", List.of());
        when(pokemonTeamService.getTeam(1)).thenReturn(team);
        when(pokemonApiMapper.toResponse(team)).thenReturn(response);

        //when
        var result = sut.getTeam(1);

        //then
        assertEquals(response, result);
        verify(pokemonTeamService).getTeam(1);
        verify(pokemonApiMapper).toResponse(team);
    }

    @Test
    void shouldUseBattleManagerAndMapperToBattle() {
        //given
        var pokemon = new Pokemon(25, "Pikachu", new Statistics(35, 55, 40, 90));
        var response = new PokemonDetailsResponseDto(25, "Pikachu",
                new StatisticsResponseDto(35, 55, 40, 90));
        when(battleManager.battle("Ash")).thenReturn(pokemon);
        when(pokemonApiMapper.toResponse(pokemon)).thenReturn(response);

        //when
        var result = sut.battle("Ash");

        //then
        assertEquals(response, result);
        verify(battleManager).battle("Ash");
        verify(pokemonApiMapper).toResponse(pokemon);
    }
}
