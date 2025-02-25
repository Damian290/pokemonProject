package com.rest.pokemon.service.feature.battle;

import com.rest.pokemon.repository.PokemonTeamDao;
import com.rest.pokemon.service.feature.battle.onevsone.OneVsOneResolver;
import com.rest.pokemon.service.feature.battle.oponentfinder.OpponentFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BattleManagerConfig {

    @Bean
    public BattleManager battleManager(
            OpponentFinder opponentFinder,
            PokemonTeamDao pokemonTeamDao
    ) {
        return new BattleManager(
                pokemonTeamDao,
                opponentFinder,
                new OneVsOneResolver());
    }
}
