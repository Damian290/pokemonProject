package com.rest.pokemon.domain.battle;

import com.rest.pokemon.domain.pokemon.Pokemon;

public class DuelRing {

    private final Pokemon myPokemon;
    private final Pokemon opponent;
    private Pokemon currentTurn;

    public DuelRing(Pokemon myPokemon, Pokemon opponent) {
        if (myPokemon.isDead() || opponent.isDead()) {
            throw new IllegalStateException("A Pokemon with zero HP cannot enter a duel");
        }
        this.myPokemon = myPokemon.copy();
        this.opponent = opponent.copy();
        this.currentTurn = getFirstAttacker();
    }

    public void performTurn() {
        if (isDuelFinished()) {
            throw new IllegalStateException("Duel is already finished");
        }

        var defender = currentTurn == myPokemon ? opponent : myPokemon;
        defender.receiveDamage(currentTurn.getAttack());

        if (!isDuelFinished()) {
            currentTurn = defender;
        }
    }

    public boolean isDuelFinished() {
        return myPokemon.isDead() || opponent.isDead();
    }

    public Pokemon getWinner() {
        ensureDuelFinished();
        return (myPokemon.isDead() ? opponent : myPokemon).copy();
    }

    public Pokemon getDefeated() {
        ensureDuelFinished();
        return (myPokemon.isDead() ? myPokemon : opponent).copy();
    }

    public boolean isMyPokemonWinner() {
        ensureDuelFinished();
        return !myPokemon.isDead();
    }

    private void ensureDuelFinished() {
        if (!isDuelFinished()) {
            throw new IllegalStateException(
                    "Duel is not finished yet"
            );
        }
    }

    private Pokemon getFirstAttacker() {
        return opponent.getSpeed() > myPokemon.getSpeed()
                ? opponent
                : myPokemon;
    }

}
