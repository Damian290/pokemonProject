package com.rest.pokemon.external.pokeapi;

import com.rest.pokemon.config.CacheConfig;
import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.external.pokeapi.dto.list.PokemonOverviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@RequiredArgsConstructor
public class PokeApiConnector {
    private final PokeApiClient pokeApiClient;

    @Retryable(
            retryFor = {ResourceAccessException.class, RestClientResponseException.class},
            exceptionExpression = "@pokeApiRetryPolicy.isRetryable(#root)",
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 6000, random = true)
    )
    @Cacheable(cacheNames = CacheConfig.POKEMON_LIST_CACHE, key = "#pokemonCount")
    public List<PokemonOverviewDto> getPokemonList(int pokemonCount) {
        var response = pokeApiClient.getPokemonList(pokemonCount);
        return response.results();
    }

    @Recover
    public List<PokemonOverviewDto> recoverPokemonList(RestClientException exception, int pokemonCount) {
        throw new PokeApiPokemonListException(pokemonCount, exception);
    }

    @Retryable(
            retryFor = {ResourceAccessException.class, RestClientResponseException.class},
            exceptionExpression = "@pokeApiRetryPolicy.isRetryable(#root)",
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 6000, random = true)
    )
    @Cacheable(cacheNames = CacheConfig.POKEMON_DETAILS_CACHE, key = "#id")
    public PokemonDto getPokemonDetails(int id) {
        return pokeApiClient.getPokemonDetails(id);
    }

    @Recover
    public PokemonDto recoverPokemonDetails(RestClientException exception, int id) {
        throw new PokeApiPokemonDetailsException(id, exception);
    }
}
