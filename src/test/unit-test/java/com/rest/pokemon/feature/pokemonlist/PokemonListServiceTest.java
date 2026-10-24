package com.rest.pokemon.feature.pokemonlist;

import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.external.pokeapi.dto.list.PokemonOverviewDto;
import com.rest.pokemon.mapper.PokemonOverviewDtoToPokemonOverviewMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PokemonListServiceTest {
    private final PokeApiConnector pokeApiConnector = mock(PokeApiConnector.class);
    private final PokemonListService sut = new PokemonListService(
            pokeApiConnector, new PokemonOverviewDtoToPokemonOverviewMapper());

    @Test
    void shouldFetchFirstGenerationAndMapResultsToDomain() {
        //given
        when(pokeApiConnector.getPokemonList(PokemonListService.FIRST_GEN_POKEMON_COUNT))
                .thenReturn(List.of(new PokemonOverviewDto("Pikachu", "/pokemon/25")));

        //when
        var result = sut.getFirstGenPokemon();

        //then
        assertEquals("Pikachu", result.getFirst().name());
        assertEquals("/pokemon/25", result.getFirst().url());
        verify(pokeApiConnector).getPokemonList(PokemonListService.FIRST_GEN_POKEMON_COUNT);
    }
}
