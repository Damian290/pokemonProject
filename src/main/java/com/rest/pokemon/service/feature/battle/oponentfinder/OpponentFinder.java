package com.rest.pokemon.service.feature.battle.oponentfinder;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Random;

@RequiredArgsConstructor
public class OpponentFinder {

    private final PokeApiConnector pokeApiConnector;
    private final Random random;

    public Mono<PokemonDetails> find() {
        var randomId = random.nextInt(1, 152);
        return this.pokeApiConnector.getPokemonDetails(randomId)
                .doOnNext(it -> System.out.println("Your opponent is: " + it));
    }

}
