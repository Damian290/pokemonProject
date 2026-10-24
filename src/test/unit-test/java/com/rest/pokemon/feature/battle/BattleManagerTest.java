package com.rest.pokemon.feature.battle;

import com.rest.pokemon.domain.battle.BattleParticipants;
import com.rest.pokemon.domain.battle.DuelResult;
import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.feature.battle.duel.DuelResolver;
import com.rest.pokemon.feature.battle.preparation.BattlePreparation;
import com.rest.pokemon.metrics.MetricService;
import com.rest.pokemon.repository.PokemonTeamDao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BattleManagerTest {

    private final PokemonTeamDao pokemonTeamDao = mock(PokemonTeamDao.class);
    private final BattlePreparation battlePreparation = mock(BattlePreparation.class);
    private final DuelResolver duelResolver = mock(DuelResolver.class);
    private final MetricService metricService = mock(MetricService.class);
    private final BattleManager sut = new BattleManager(
            pokemonTeamDao, battlePreparation, duelResolver, metricService, new TeamBattleLock());
    private final PokemonDetails myPokemon = pokemon(1, "Pikachu");
    private final PokemonDetails opponent = pokemon(2, "Charmander");

    @Test
    void shouldUpdateMyPokemonAndIncrementMetricWhenItWins() {
        //given
        var participants = new BattleParticipants(11, myPokemon, opponent);
        when(battlePreparation.prepare("team")).thenReturn(participants);
        when(duelResolver.resolve(myPokemon, opponent)).thenReturn(new DuelResult(myPokemon, true));

        //when
        var winner = sut.battle("team");

        //then
        assertSame(myPokemon, winner);
        verify(battlePreparation).prepare("team");
        verify(duelResolver).resolve(myPokemon, opponent);
        verify(pokemonTeamDao).updateFirstPokemon(11, myPokemon);
        verify(pokemonTeamDao, never()).removeFirst(anyLong());
        verify(metricService).incrementDefeatedOpponents();
    }

    @Test
    void shouldRemoveFirstTeamMemberWhenOpponentWins() {
        //given
        when(battlePreparation.prepare("team"))
                .thenReturn(new BattleParticipants(11, myPokemon, opponent));
        when(duelResolver.resolve(myPokemon, opponent)).thenReturn(new DuelResult(opponent, false));
        when(pokemonTeamDao.removeFirst(11)).thenReturn(myPokemon);

        //when
        var winner = sut.battle("team");

        //then
        assertSame(opponent, winner);
        verify(battlePreparation).prepare("team");
        verify(duelResolver).resolve(myPokemon, opponent);
        verify(pokemonTeamDao).removeFirst(11);
        verify(pokemonTeamDao, never()).updateFirstPokemon(anyLong(), any());
        verifyNoInteractions(metricService);
    }


    private PokemonDetails pokemon(int id, String name) {
        return new PokemonDetails(id, name, new Statistics(50, 55, 40, 90));
    }
}
