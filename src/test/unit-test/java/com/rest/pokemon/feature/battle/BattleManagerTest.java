package com.rest.pokemon.feature.battle;

import com.rest.pokemon.domain.battle.BattleParticipants;
import com.rest.pokemon.domain.battle.DuelResult;
import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.feature.battle.duel.DuelResolver;
import com.rest.pokemon.feature.battle.preparation.BattlePreparation;
import com.rest.pokemon.metrics.MetricService;
import com.rest.pokemon.repository.PokemonTeamDao;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class BattleManagerTest {

    private final PokemonTeamDao pokemonTeamDao = mock(PokemonTeamDao.class);
    private final BattlePreparation battlePreparation = mock(BattlePreparation.class);
    private final DuelResolver duelResolver = mock(DuelResolver.class);
    private final MetricService metricService = mock(MetricService.class);
    private final BattleManager sut = new BattleManager(
            pokemonTeamDao, battlePreparation, duelResolver, metricService, new TeamBattleLock());
    private final Pokemon myPokemon = pokemon(1, "Pikachu");
    private final Pokemon opponent = pokemon(2, "Charmander");

    @Test
    void shouldSaveMyPokemonStateAndIncrementMetricWhenItWins() {
        //given
        var team = new PokemonTeam(11, "team", List.of(myPokemon));
        var winner = new Pokemon(1, "Pikachu", new Statistics(30, 55, 40, 90));
        when(battlePreparation.prepare("team"))
                .thenReturn(new BattleParticipants(11, myPokemon, opponent));
        when(duelResolver.resolve(myPokemon, opponent))
                .thenReturn(new DuelResult(winner, opponent, true));
        when(pokemonTeamDao.getById(11)).thenReturn(team);

        //when
        var result = sut.battle("team");

        //then
        assertSame(winner, result);
        verify(battlePreparation).prepare("team");
        verify(duelResolver).resolve(myPokemon, opponent);
        var savedTeamCaptor = ArgumentCaptor.forClass(PokemonTeam.class);
        verify(pokemonTeamDao).save(savedTeamCaptor.capture());
        assertThat(savedTeamCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(new PokemonTeam(11, "team", List.of(winner)));
        verify(metricService).incrementDefeatedOpponents();
    }

    @Test
    void shouldSaveTeamWithoutDefeatedPokemonWhenOpponentWins() {
        //given
        var secondPokemon = pokemon(3, "Bulbasaur");
        var team = new PokemonTeam(11, "team", List.of(myPokemon, secondPokemon));
        when(battlePreparation.prepare("team"))
                .thenReturn(new BattleParticipants(11, myPokemon, opponent));
        when(duelResolver.resolve(myPokemon, opponent))
                .thenReturn(new DuelResult(opponent, myPokemon, false));
        when(pokemonTeamDao.getById(11)).thenReturn(team);

        //when
        var result = sut.battle("team");

        //then
        assertSame(opponent, result);
        verify(battlePreparation).prepare("team");
        verify(duelResolver).resolve(myPokemon, opponent);
        var savedTeamCaptor = ArgumentCaptor.forClass(PokemonTeam.class);
        verify(pokemonTeamDao).save(savedTeamCaptor.capture());
        assertThat(savedTeamCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(new PokemonTeam(11, "team", List.of(secondPokemon)));
        verifyNoInteractions(metricService);
    }

    private Pokemon pokemon(int id, String name) {
        return new Pokemon(id, name, new Statistics(50, 55, 40, 90));
    }
}
