package com.rest.pokemon.external.pokeapi;

import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.external.pokeapi.dto.list.PokemonListDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

@HttpExchange("/pokemon")
public interface PokeApiClient {

    @GetExchange()
    Mono<PokemonListDto> getPokemonList(@RequestParam int limit);

    @GetExchange("/{id}")
    Mono<PokemonDto> getPokemonDetails(@PathVariable Integer id);
}

