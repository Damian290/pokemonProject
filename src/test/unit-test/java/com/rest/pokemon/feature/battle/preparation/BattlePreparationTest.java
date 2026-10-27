package com.rest.pokemon.feature.battle.preparation;

import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.feature.battle.exception.NoPokemonTeamException;
import com.rest.pokemon.feature.battle.opponentfinder.OpponentFinder;
import com.rest.pokemon.repository.PokemonTeamDao;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BattlePreparationTest {
    private final PokemonTeamDao pokemonTeamDao = mock(PokemonTeamDao.class);
    private final OpponentFinder opponentFinder = mock(OpponentFinder.class);
    private final BattlePreparation sut = new BattlePreparation(pokemonTeamDao, opponentFinder);

    @Test
    void shouldUseFirstTeamMemberAndFindOpponent() {
        //given
        var firstPokemon = new Pokemon(1, "Pikachu", new Statistics(50, 20, 10, 100));
        var secondPokemon = new Pokemon(2, "Charmander", new Statistics(50, 20, 10, 80));
        var opponent = new Pokemon(3, "Bulbasaur", new Statistics(50, 20, 10, 70));
        var members = List.of(firstPokemon, secondPokemon);
        when(pokemonTeamDao.getByName("team")).thenReturn(new PokemonTeam(7, "team", members));
        when(opponentFinder.find(anyList())).thenReturn(opponent);

        //when
        var participants = sut.prepare("team");

        //then
        assertEquals(7, participants.teamId());
        assertThat(participants.myPokemon()).usingRecursiveComparison().isEqualTo(firstPokemon);
        assertSame(opponent, participants.opponentPokemon());
        var membersCaptor = ArgumentCaptor.forClass(List.class);
        verify(opponentFinder).find(membersCaptor.capture());
        assertThat(membersCaptor.getValue()).usingRecursiveComparison().isEqualTo(members);
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
