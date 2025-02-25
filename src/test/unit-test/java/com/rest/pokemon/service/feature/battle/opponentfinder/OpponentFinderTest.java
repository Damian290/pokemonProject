package com.rest.pokemon.service.feature.battle.opponentfinder;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.service.feature.battle.oponentfinder.OpponentFinder;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Random;

import static org.mockito.Mockito.*;

public class OpponentFinderTest {

    private final PokeApiConnector pokeApiConnector = mock(PokeApiConnector.class);
    private final Random random = mock(Random.class);
    private final OpponentFinder sut = new OpponentFinder(pokeApiConnector, random);

    @Test
    void shouldReturnPokemonDetailsFromApi() {
        //given
        int randomId = 25;
        PokemonDetails mockPokemon = new PokemonDetails(25, "Pikachu", null);
        when(random.nextInt(1, 152)).thenReturn(randomId);
        when(pokeApiConnector.getPokemonDetails(randomId)).thenReturn(Mono.just(mockPokemon));

        // When & Then
        StepVerifier.create(sut.find())
                .expectNext(mockPokemon)
                .verifyComplete();

        verify(random, times(1)).nextInt(1, 152);
        verify(pokeApiConnector, times(1)).getPokemonDetails(randomId);
    }

    @Test
    void shouldPropagateErrorWhenApiFails() {
        // Given
        int randomId = 50;
        when(random.nextInt(1, 152)).thenReturn(randomId);
        when(pokeApiConnector.getPokemonDetails(randomId))
                .thenReturn(Mono.error(new RuntimeException("API failure")));

        // When & Then
        StepVerifier.create(sut.find())
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("API failure"))
                .verify();

        verify(random, times(1)).nextInt(1, 152);
        verify(pokeApiConnector, times(1)).getPokemonDetails(randomId);
    }
}
