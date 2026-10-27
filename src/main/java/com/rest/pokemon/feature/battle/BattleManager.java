package com.rest.pokemon.feature.battle;

import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.battle.DuelResult;
import com.rest.pokemon.repository.PokemonTeamDao;
import com.rest.pokemon.feature.battle.duel.DuelResolver;
import com.rest.pokemon.feature.battle.preparation.BattlePreparation;
import com.rest.pokemon.metrics.MetricService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class BattleManager {

    private final PokemonTeamDao pokemonTeamDao;
    private final BattlePreparation battlePreparation;
    private final DuelResolver duelResolver;
    private final MetricService metricService;
    private final TeamBattleLock teamBattleLock;

    public Pokemon battle(String teamName) {
        return teamBattleLock.execute(teamName, () -> resolveBattle(teamName));
    }

    private Pokemon resolveBattle(String teamName) {
        var participants = battlePreparation.prepare(teamName);
        var teamId = participants.teamId();

        var duelResult = duelResolver.resolve(
                participants.myPokemon(), participants.opponentPokemon()
        );

        applyDuelResult(teamId, duelResult);
        log.info("Duel finished: winnerId={}, myPokemonWinner={}",
                duelResult.winner().id(), duelResult.myPokemonWinner());
        return duelResult.winner();
    }

    private void applyDuelResult(long teamId, DuelResult duelResult) {
        var team = pokemonTeamDao.getById(teamId);
        if (!duelResult.myPokemonWinner()) {
            pokemonTeamDao.save(team.removeDefeatedPokemon(duelResult.defeated()));
            log.debug("Defeated Pokemon removed from team: {}", duelResult.defeated());
        } else {
            pokemonTeamDao.save(team.updatePokemonState(duelResult.winner()));
            metricService.incrementDefeatedOpponents();
        }
    }
}
