package com.rest.pokemon.feature.pokemonteam.idselector;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RandomPokemonIdsSelectorTest {

    private final Random random = mock(Random.class);
    private final RandomPokemonIdsSelector sut = new RandomPokemonIdsSelector(random);

    @Test
    void shouldSelectSixUniquePokemonIdsInFirstGenerationRange() {
        //given
        when(random.ints(1, 152)).thenReturn(IntStream.of(1, 1, 2, 3, 4, 5, 6));

        //when
        List<Integer> selectedIds = sut.selectSixIds();

        //then
        assertEquals(List.of(1, 2, 3, 4, 5, 6), selectedIds);
    }
}
