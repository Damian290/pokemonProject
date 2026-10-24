package com.rest.pokemon.api;

import com.rest.pokemon.api.dto.PokemonDetailsResponseDto;
import com.rest.pokemon.api.dto.PokemonTeamResponseDto;
import com.rest.pokemon.feature.battle.BattleManager;
import com.rest.pokemon.feature.pokemonteam.PokemonTeamCreator;
import com.rest.pokemon.feature.pokemonteam.PokemonTeamService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Pokemon team", description = "Team management and battles")
public class PokemonTeamEndpoint {

    private final PokemonTeamCreator pokemonTeamCreator;
    private final BattleManager battleManager;
    private final PokemonTeamService pokemonTeamService;
    private final PokemonApiMapper pokemonApiMapper;

    @GetMapping("/v1/pokemon/team")
    @Operation(summary = "Get all Pokemon teams")
    public List<PokemonTeamResponseDto> getTeams() {
        return pokemonApiMapper.toTeamResponses(pokemonTeamService.getTeams());
    }

    @PostMapping("/v1/pokemon/team")
    @Operation(
            summary = "Create a named team of six random Pokemon",
            description = "Creates and stores a new team with the supplied name."
    )
    @ResponseStatus(HttpStatus.CREATED)
    public PokemonTeamResponseDto createTeam(
            @RequestParam @NotBlank @Size(max = 20) String name) {
        return pokemonApiMapper.toResponse(pokemonTeamCreator.create(name));
    }

    @GetMapping("/v1/pokemon/team/{teamId}")
    @Operation(summary = "Get a Pokemon team by ID")
    public PokemonTeamResponseDto getTeam(@PathVariable long teamId) {
        return pokemonApiMapper.toResponse(pokemonTeamService.getTeam(teamId));
    }

    @PutMapping("/v1/pokemon/team/battle")
    @Operation(summary = "Battle a random opponent with the selected team")
    public PokemonDetailsResponseDto battle(@RequestParam String name) {
        return pokemonApiMapper.toResponse(battleManager.battle(name));
    }

}
