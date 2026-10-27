package com.rest.pokemon.feature.battle.duel;

import com.rest.pokemon.domain.battle.DuelResult;
import com.rest.pokemon.domain.battle.DuelRing;
import com.rest.pokemon.domain.pokemon.Pokemon;

public class DuelResolver {

    public DuelResult resolve(Pokemon myPokemon, Pokemon opponentPokemon) {
        var duelRing = new DuelRing(myPokemon, opponentPokemon);

        fight(duelRing);

        return new DuelResult(
                duelRing.getWinner().afterDuel(),
                duelRing.getDefeated().afterDuel(),
                duelRing.isMyPokemonWinner()
        );
    }

    private void fight(DuelRing duelRing) {
        while (!duelRing.isDuelFinished()) {
            duelRing.performTurn();
        }
    }

}
