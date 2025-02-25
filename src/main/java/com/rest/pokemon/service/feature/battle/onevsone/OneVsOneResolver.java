package com.rest.pokemon.service.feature.battle.onevsone;

import com.rest.pokemon.domain.pokemon.PokemonDetails;

public class OneVsOneResolver {

    public OneVsOneRing resolve(PokemonDetails myPokemonDetails, PokemonDetails opponentPokemonDetails) {
        var battleRing = new OneVsOneRing(myPokemonDetails, opponentPokemonDetails);

        battleRing.printRingState();
        oneVsOneFight(battleRing);
        battleRing.printRingState();

        return battleRing;
    }

    private void oneVsOneFight(OneVsOneRing oneVsOneRing) {
        while (attack(oneVsOneRing, oneVsOneRing.getCurrentTurn().getPokemonDetails().statistics().getAttack())) {
            oneVsOneRing.setCurrentTurn(oneVsOneRing.damageReceiver());
            oneVsOneRing.printRingState();
        }
    }

    private Boolean attack(OneVsOneRing oneVsOneRing, long damage) {
        var damageReceiver = oneVsOneRing.damageReceiver();
        if (damageReceiver.getCurrentDefenceLvl() > 0) {
            damageReceiver.setCurrentDefenceLvl(damageReceiver.getCurrentDefenceLvl() - oneVsOneRing.getCurrentTurn().getPokemonDetails().statistics().getAttack());
        } else {
            damageReceiver.getPokemonDetails().statistics().setHp(damageReceiver.getPokemonDetails().statistics().getHp() - damage);
        }
        return damageReceiver.getPokemonDetails().statistics().getHp() > 0;
    }
}
