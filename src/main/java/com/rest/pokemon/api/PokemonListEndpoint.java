package com.rest.pokemon.api;

import com.rest.pokemon.domain.pokemon.PokemonOverview;
import com.rest.pokemon.service.feature.pokemonlist.PokemonListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
public class PokemonListEndpoint {

    private final PokemonListService pokemonListService;

    @GetMapping("/v1/pokemon")
    public Flux<PokemonOverview> getAllPokemon() {
        return pokemonListService.getFirstGenPokemon();
    }
}