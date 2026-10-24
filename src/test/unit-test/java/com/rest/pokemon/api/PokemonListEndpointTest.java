package com.rest.pokemon.api;

import com.rest.pokemon.api.dto.PokemonOverviewResponseDto;
import com.rest.pokemon.domain.pokemon.PokemonOverview;
import com.rest.pokemon.feature.pokemonlist.PokemonListService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PokemonListEndpointTest {
    private final PokemonListService pokemonListService = mock(PokemonListService.class);
    private final PokemonApiMapper pokemonApiMapper = mock(PokemonApiMapper.class);
    private final PokemonListEndpoint sut = new PokemonListEndpoint(pokemonListService, pokemonApiMapper);

    @Test
    void shouldUsePokemonListServiceAndMapperToGetAllPokemon() {
        //given
        var pokemon = List.of(new PokemonOverview("Pikachu", "/pokemon/25"));
        var expectedResponse = List.of(new PokemonOverviewResponseDto("Pikachu", "/pokemon/25"));
        when(pokemonListService.getFirstGenPokemon()).thenReturn(pokemon);
        when(pokemonApiMapper.toOverviewResponses(pokemon)).thenReturn(expectedResponse);

        //when
        var result = sut.getAllPokemon();

        //then
        assertEquals(expectedResponse, result);
        verify(pokemonListService).getFirstGenPokemon();
        verify(pokemonApiMapper).toOverviewResponses(pokemon);
    }
}
