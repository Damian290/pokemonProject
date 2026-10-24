package com.rest.pokemon.feature.pokemonlist;

import com.rest.pokemon.domain.pokemon.PokemonOverview;
import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.mapper.PokemonOverviewDtoToPokemonOverviewMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PokemonListService {

    public static final int FIRST_GEN_POKEMON_COUNT = 151;
    private final PokeApiConnector pokeApiConnector;
    private final PokemonOverviewDtoToPokemonOverviewMapper pokemonMapper;

    public List<PokemonOverview> getFirstGenPokemon() {
        return pokeApiConnector.getPokemonList(FIRST_GEN_POKEMON_COUNT).stream()
                .map(pokemonMapper::map)
                .toList();
    }
}


