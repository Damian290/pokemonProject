package com.rest.pokemon.external.pokeapi;

import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.external.pokeapi.dto.list.PokemonListDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/pokemon")
public interface PokeApiClient {

    @GetExchange()
    PokemonListDto getPokemonList(@RequestParam int limit);

    @GetExchange("/{id}")
    PokemonDto getPokemonDetails(@PathVariable Integer id);
}

