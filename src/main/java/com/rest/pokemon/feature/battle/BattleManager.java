package com.rest.pokemon.feature.battle;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
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

    public PokemonDetails battle(String teamName) {
        return teamBattleLock.execute(teamName, () -> resolveBattle(teamName));
    }

    private PokemonDetails resolveBattle(String teamName) {
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
        if (!duelResult.myPokemonWinner()) {
            var removedPokemon = pokemonTeamDao.removeFirst(teamId);
            log.debug("Defeated Pokemon removed from team: {}", removedPokemon);
        } else {
            pokemonTeamDao.updateFirstPokemon(teamId, duelResult.winner());
            metricService.incrementDefeatedOpponents();
        }
    }
}
