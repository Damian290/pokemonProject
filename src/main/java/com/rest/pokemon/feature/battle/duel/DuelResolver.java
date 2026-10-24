package com.rest.pokemon.feature.battle.duel;

import com.rest.pokemon.domain.battle.DuelResult;
import com.rest.pokemon.domain.battle.DuelRing;
import com.rest.pokemon.domain.battle.PokemonInDuel;
import com.rest.pokemon.domain.pokemon.PokemonDetails;

public class DuelResolver {

    public DuelResult resolve(PokemonDetails myPokemonDetails, PokemonDetails opponentPokemonDetails) {
        var duelRing = new DuelRing(myPokemonDetails, opponentPokemonDetails);

        fight(duelRing);

        return new DuelResult(
                duelRing.getWinner().getPokemonDetails(),
                duelRing.isMyPokemonWinner()
        );
    }

    private void fight(DuelRing duelRing) {
        while (!duelRing.isDuelFinished()) {
            var attacker = duelRing.getCurrentTurn();
            var defender = duelRing.damageReceiver();

            attack(attacker, defender);

            if (!duelRing.isDuelFinished()) {
                duelRing.setCurrentTurn(defender);
            }
        }
    }

    private void attack(PokemonInDuel attacker, PokemonInDuel defender) {
        var damage = attacker.getPokemonDetails().statistics().getAttack();
        if (defender.getCurrentDefenceLvl() > 0) {
            defender.setCurrentDefenceLvl(defender.getCurrentDefenceLvl() - damage);
        } else {
            var defenderStatistics = defender.getPokemonDetails().statistics();
            defender.setCurrentHp(defenderStatistics.getHp() - damage);
        }
    }
}
