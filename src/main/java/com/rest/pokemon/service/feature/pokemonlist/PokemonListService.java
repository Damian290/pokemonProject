package com.rest.pokemon.service.feature.pokemonlist;

import com.rest.pokemon.domain.pokemon.PokemonOverview;
import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class PokemonListService {

    public static final int POKEMON_NR = 151;
    private final PokeApiConnector pokeApiConnector;

    public Flux<PokemonOverview> getFirstGenPokemon() {
        return pokeApiConnector.getPokemonList(POKEMON_NR);
    }
}


