package com.rest.pokemon.mapper;

import com.rest.pokemon.external.pokeapi.dto.list.PokemonOverviewDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PokemonOverviewDtoToPokemonOverviewMapperTest {
    private final PokemonOverviewDtoToPokemonOverviewMapper sut = new PokemonOverviewDtoToPokemonOverviewMapper();

    @Test
    void shouldMapNameAndUrl() {
        //given
        var dto = new PokemonOverviewDto("Pikachu", "https://pokeapi.co/api/v2/pokemon/25/");

        //when
        var pokemon = sut.map(dto);

        //then
        assertEquals("Pikachu", pokemon.name());
        assertEquals(dto.url(), pokemon.url());
    }
}
