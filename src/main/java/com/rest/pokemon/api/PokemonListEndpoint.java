package com.rest.pokemon.api;

import com.rest.pokemon.api.dto.PokemonOverviewResponseDto;
import com.rest.pokemon.feature.pokemonlist.PokemonListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PokemonListEndpoint {

    private final PokemonListService pokemonListService;
    private final PokemonApiMapper pokemonApiMapper;

    @GetMapping("/v1/pokemon")
    public List<PokemonOverviewResponseDto> getAllPokemon() {
        return pokemonApiMapper.toOverviewResponses(pokemonListService.getFirstGenPokemon());
    }
}