package com.rest.pokemon.service.feature.pokemonteam;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.repository.PokemonTeam;
import com.rest.pokemon.repository.PokemonTeamDao;
import com.rest.pokemon.service.feature.pokemonteam.idselector.PokemonIdsSelector;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class PokemonTeamCreatorTest {

    private final PokemonIdsSelector pokemonIdsSelector = mock(PokemonIdsSelector.class);
    private final PokeApiConnector pokeApiConnector = mock(PokeApiConnector.class);
    private final PokemonTeamDao pokemonTeamDao = mock(PokemonTeamDao.class);
    private final PokemonTeamCreator sut = new PokemonTeamCreator(pokemonIdsSelector, pokeApiConnector, pokemonTeamDao);

    @Test
    void shouldCreatePokemonTeamSuccessfully() {
        // Given
        List<Integer> pokemonIds = List.of(1, 2, 3, 4, 5, 6);
        when(pokemonIdsSelector.selectSixIds()).thenReturn(pokemonIds);

        List<PokemonDetails> pokemonDetailsList = pokemonIds.stream()
                .map(id -> createPokemon(id, "Pokemon", 50, 20, 10, 15))
                .toList();

        when(pokeApiConnector.getPokemonDetails(anyInt()))
                .thenAnswer(invocation -> Mono.just(createPokemon(invocation.getArgument(0), "Pokemon", 50, 20, 10, 15)));

        PokemonTeam expectedTeam = new PokemonTeam(pokemonDetailsList);
        when(pokemonTeamDao.create(anyList())).thenReturn(expectedTeam);

        // When
        Mono<PokemonTeam> result = sut.create();

        // Then
        StepVerifier.create(result)
                .expectNext(expectedTeam)
                .verifyComplete();

        verify(pokemonIdsSelector, times(1)).selectSixIds();
        verify(pokeApiConnector, times(6)).getPokemonDetails(anyInt());
        verify(pokemonTeamDao, times(1)).create(pokemonDetailsList);
    }

    private PokemonDetails createPokemon(int id, String name, long hp, long attack, long defence, long speed) {
        Statistics stats = new Statistics(hp, attack, defence, speed);
        return new PokemonDetails(id, name, stats);
    }
}

