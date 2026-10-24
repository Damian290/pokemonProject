package com.rest.pokemon.external.pokeapi;

import com.rest.pokemon.external.pokeapi.dto.details.PokemonDto;
import com.rest.pokemon.external.pokeapi.dto.list.PokemonListDto;
import com.rest.pokemon.external.pokeapi.dto.list.PokemonOverviewDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PokeApiConnectorTest {
    private final PokeApiClient pokeApiClient = mock(PokeApiClient.class);
    private final PokeApiConnector sut = new PokeApiConnector(pokeApiClient);

    @Test
    void shouldReturnExternalPokemonListDtos() {
        //given
        int pokemonCount = 10;
        var pokemonList = new PokemonListDto(List.of(
                new PokemonOverviewDto("Pikachu", "url1"),
                new PokemonOverviewDto("Bulbasaur", "url2")));
        when(pokeApiClient.getPokemonList(pokemonCount)).thenReturn(pokemonList);

        //when
        var result = sut.getPokemonList(pokemonCount);

        //then
        assertEquals(pokemonList.results(), result);
        verify(pokeApiClient).getPokemonList(pokemonCount);
    }

    @Test
    void shouldReturnExternalPokemonDetailsDto() {
        //given
        int pokemonId = 25;
        var pokemonDto = new PokemonDto("Pikachu", List.of());
        when(pokeApiClient.getPokemonDetails(pokemonId)).thenReturn(pokemonDto);

        //when
        var result = sut.getPokemonDetails(pokemonId);

        //then
        assertEquals(pokemonDto, result);
        verify(pokeApiClient).getPokemonDetails(pokemonId);
    }
}
