package com.rest.pokemon.api;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.repository.PokemonTeam;
import com.rest.pokemon.service.feature.battle.BattleManager;
import com.rest.pokemon.service.feature.pokemonteam.PokemonTeamCreator;
import com.rest.pokemon.service.feature.pokemonteam.PokemonTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class PokemonTeamEndpoint {

    private final PokemonTeamCreator pokemonTeamCreator;
    private final BattleManager battleManager;
    private final PokemonTeamService pokemonTeamService;


    @GetMapping("/v1/pokemon/team")
    public Mono<PokemonTeam> getMyPokemon() {
        return pokemonTeamService.getTeam();
    }

    @PostMapping("/v1/pokemon/team")
    public Mono<PokemonTeam> createTeam() {
        return pokemonTeamCreator.create();
    }

    @PutMapping("/v1/pokemon/team/battle")
    public Mono<PokemonDetails> battle() {
        return battleManager.battle();
    }

}
