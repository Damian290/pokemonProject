package com.rest.pokemon.external.pokeapi;

import com.github.benmanes.caffeine.cache.Cache;
import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.PokemonOverview;
import com.rest.pokemon.service.mapper.PokemonDtoToPokemonDetailsMapper;
import com.rest.pokemon.service.mapper.PokemonOverviewDtoToPokemonOverviewMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@RequiredArgsConstructor
public class PokeApiConnector {

    private final PokeApiClient pokeApiClient;
    private final PokemonOverviewDtoToPokemonOverviewMapper pokemonOverviewDtoToPokemonOverviewMapper;
    private final PokemonDtoToPokemonDetailsMapper pokemonDtoToPokemonDetailsMapper;
    private final Cache<Integer, PokemonOverview> pokemonListCache;
    private final Retry pokeApiRetrySpec;

    public Flux<PokemonOverview> getPokemonList(int pokemonNr) {
        PokemonOverview cachedPokemonList = pokemonListCache.getIfPresent(pokemonNr);
        if (cachedPokemonList != null) {
            return Flux.just(cachedPokemonList);
        }
        return pokeApiClient.getPokemonList(pokemonNr)
                .retryWhen(pokeApiRetrySpec)
                .flatMapMany(response -> Flux.fromIterable(response.results()))
                .map(pokemonOverviewDtoToPokemonOverviewMapper::map)
                .doOnNext(result -> pokemonListCache.put(pokemonNr, result));

    }

    public Mono<PokemonDetails> getPokemonDetails(int id) {
        return pokeApiClient.getPokemonDetails(id)
                .map(it -> pokemonDtoToPokemonDetailsMapper.map(it, id));
    }
}
