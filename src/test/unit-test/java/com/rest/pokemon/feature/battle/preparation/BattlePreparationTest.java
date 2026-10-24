package com.rest.pokemon.feature.battle.preparation;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.feature.battle.exception.NoPokemonTeamException;
import com.rest.pokemon.feature.battle.oponentfinder.OpponentFinder;
import com.rest.pokemon.repository.PokemonTeamDao;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BattlePreparationTest {
    private final PokemonTeamDao pokemonTeamDao = mock(PokemonTeamDao.class);
    private final OpponentFinder opponentFinder = mock(OpponentFinder.class);
    private final BattlePreparation sut = new BattlePreparation(pokemonTeamDao, opponentFinder);

    @Test
    void shouldUseFirstTeamMemberAndFindOpponent() {
        //given
        var firstPokemon = PokemonDetails.builder().id(1).build();
        var secondPokemon = PokemonDetails.builder().id(2).build();
        var opponent = PokemonDetails.builder().id(3).build();
        var members = List.of(firstPokemon, secondPokemon);
        when(pokemonTeamDao.getByName("team")).thenReturn(new PokemonTeam(7, "team", members));
        when(opponentFinder.find(members)).thenReturn(opponent);

        //when
        var participants = sut.prepare("team");

        //then
        assertEquals(7, participants.teamId());
        assertSame(firstPokemon, participants.myPokemon());
        assertSame(opponent, participants.opponentPokemon());
        verify(opponentFinder).find(members);
    }

    @Test
    void shouldRejectEmptyTeamWithoutSearchingForOpponent() {
        //given
        when(pokemonTeamDao.getByName("team")).thenReturn(new PokemonTeam(7, "team", List.of()));

        //when
        var exception = assertThrows(NoPokemonTeamException.class, () -> sut.prepare("team"));

        //then
        assertNotNull(exception);
        verifyNoInteractions(opponentFinder);
    }
}
