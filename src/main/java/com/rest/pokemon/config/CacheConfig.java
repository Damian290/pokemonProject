package com.rest.pokemon.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {
    public static final String POKEMON_LIST_CACHE = "pokemon-list";
    public static final String POKEMON_DETAILS_CACHE = "pokemon-details";

    @Bean
    public CacheManager cacheManager() {
        var cacheManager = new SimpleCacheManager();
        var pokemonListCache = new CaffeineCache(
                POKEMON_LIST_CACHE,
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .maximumSize(10)
                        .build()
        );
        var pokemonDetailsCache = new CaffeineCache(
                POKEMON_DETAILS_CACHE,
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .maximumSize(2000)
                        .build()
        );
        cacheManager.setCaches(java.util.List.of(pokemonListCache, pokemonDetailsCache));
        return cacheManager;
    }
}
