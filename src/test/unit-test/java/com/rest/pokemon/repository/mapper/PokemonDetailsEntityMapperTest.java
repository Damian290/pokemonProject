package com.rest.pokemon.repository.mapper;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.repository.entity.PokemonDetailsEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PokemonDetailsEntityMapperTest {
    private final PokemonDetailsEntityMapper sut = new PokemonDetailsEntityMapper();

    @Test
    void shouldMapDomainPokemonToEntity() {
        //given
        var pokemon = new PokemonDetails(25, "Pikachu", new Statistics(35, 55, 40, 90));

        //when
        var entity = sut.toEntity(pokemon);

        //then
        assertEquals(new PokemonDetailsEntity(25, "Pikachu", 35, 55, 40, 90), entity);
    }

    @Test
    void shouldMapEntityToIndependentDomainPokemon() {
        //given
        var entity = new PokemonDetailsEntity(25, "Pikachu", 35, 55, 40, 90);

        //when
        var pokemon = sut.toDomain(entity);

        //then
        assertEquals(25, pokemon.id());
        assertEquals("Pikachu", pokemon.name());
        assertEquals(new Statistics(35, 55, 40, 90), pokemon.statistics());
    }
}
