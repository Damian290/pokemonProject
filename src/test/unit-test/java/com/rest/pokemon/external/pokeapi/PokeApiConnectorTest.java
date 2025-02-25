package com.rest.pokemon.external.pokeapi;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.PokemonOverview;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.external.pokeapi.dto.list.PokemonListDto;
import com.rest.pokemon.external.pokeapi.dto.list.PokemonOverviewDto;
import com.rest.pokemon.service.mapper.PokemonDtoToPokemonDetailsMapper;
import com.rest.pokemon.service.mapper.PokemonOverviewDtoToPokemonOverviewMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

import static org.mockito.Mockito.*;

public class PokeApiConnectorTest {

    private final PokeApiClient pokeApiClient = mock(PokeApiClient.class);
    private final PokemonOverviewDtoToPokemonOverviewMapper pokemonOverviewMapper = mock(PokemonOverviewDtoToPokemonOverviewMapper.class);
    private final PokemonDtoToPokemonDetailsMapper pokemonDetailsMapper = mock(PokemonDtoToPokemonDetailsMapper.class);
    private final Retry retry = Retry.backoff(3, Duration.ofMillis(100));
    private final PokeApiConnector sut = new PokeApiConnector(
            pokeApiClient,
            pokemonOverviewMapper,
            pokemonDetailsMapper,
            retry
    );

    @BeforeEach
    void setUp() {

    }

    @Test
    void shouldReturnPokemonListSuccessfully() {
        // Given
        int pokemonNr = 10;
        PokemonOverviewDto pokemonOverviewDto = new PokemonOverviewDto("Pikachu", "url");
        PokemonListDto dto = new PokemonListDto(List.of(pokemonOverviewDto));
        when(pokeApiClient.getPokemonList(pokemonNr)).thenReturn(Mono.just(dto));
        when(pokemonOverviewMapper.map(pokemonOverviewDto)).thenReturn(new PokemonOverview("Pikachu", "url"));

        // When
        Flux<PokemonOverview> result = sut.getPokemonList(pokemonNr);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(pokemon -> "Pikachu".equals(pokemon.name()))
                .verifyComplete();

        verify(pokeApiClient, times(1)).getPokemonList(pokemonNr);
    }


    @Test
    void shouldReturnPokemonDetailsSuccessfully() {
        // Given
        int pokemonId = 25;
        PokemonDto mockResponse = new PokemonDto("Pikachu", List.of());
        PokemonDetails expectedDetails = new PokemonDetails(pokemonId, "Pikachu", Statistics.builder().build());

        when(pokeApiClient.getPokemonDetails(pokemonId)).thenReturn(Mono.just(mockResponse));
        when(pokemonDetailsMapper.map(mockResponse, pokemonId)).thenReturn(expectedDetails);

        // When
        Mono<PokemonDetails> result = sut.getPokemonDetails(pokemonId);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(it -> it.equals(expectedDetails))
                .verifyComplete();

        verify(pokeApiClient, times(1)).getPokemonDetails(pokemonId);
    }
}
