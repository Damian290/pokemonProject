package com.rest.pokemon.feature.battle;

import com.rest.pokemon.feature.battle.duel.DuelResolver;
import com.rest.pokemon.feature.battle.oponentfinder.OpponentFinder;
import com.rest.pokemon.feature.battle.preparation.BattlePreparation;
import com.rest.pokemon.metrics.MetricService;
import com.rest.pokemon.repository.PokemonTeamDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BattleManagerConfig {

    @Bean
    public BattlePreparation battlePreparation(PokemonTeamDao pokemonTeamDao, OpponentFinder opponentFinder) {
        return new BattlePreparation(pokemonTeamDao, opponentFinder);
    }

    @Bean
    public BattleManager battleManager(
            BattlePreparation battlePreparation,
            PokemonTeamDao pokemonTeamDao,
            MetricService metricService,
            TeamBattleLock teamBattleLock
    ) {
        return new BattleManager(
                pokemonTeamDao,
                battlePreparation,
                new DuelResolver(),
                metricService,
                teamBattleLock);
    }
}
