package com.rest.pokemon.service.feature.battle.onevsone;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import lombok.Data;

@Data
public class OneVsOneRing {

    public OneVsOneRing(PokemonDetails myPokemonDetails, PokemonDetails opponent) {
        this.myPokemon = new PokemonInDuel(myPokemonDetails, myPokemonDetails.statistics().getDefence());
        this.opponent = new PokemonInDuel(opponent, opponent.statistics().getDefence());
        this.currentTurn = getFirstAttacker();
    }

    private final PokemonInDuel myPokemon;
    private final PokemonInDuel opponent;
    private PokemonInDuel currentTurn;

    public PokemonInDuel damageReceiver() {
        return currentTurn.equals(myPokemon) ? opponent : myPokemon;
    }

    public PokemonInDuel getWinner() {
        if (myPokemon.getPokemonDetails().statistics().getHp() <= 0) {
            return opponent;
        } else if (opponent.getPokemonDetails().statistics().getHp() <= 0) {
            return myPokemon;
        } else {
            return null;
        }
    }

    public Boolean isMyPokemonWinner() {
        return getWinner().equals(myPokemon);
    }

    //For debuging purposes
    public void printRingState() {
        System.out.println("MyPokemon stats: ");
        System.out.println("Name: " + myPokemon.getPokemonDetails().name());
        System.out.println("Hp: " + myPokemon.getPokemonDetails().statistics().getHp());
        System.out.println("Defense: " + myPokemon.getCurrentDefenceLvl());
        System.out.println("Speed: " + myPokemon.getPokemonDetails().statistics().getSpeed());
        System.out.println("---------VS-----------");
        System.out.println("Opponent stats: ");
        System.out.println("Name: " + opponent.getPokemonDetails().name());
        System.out.println("Hp: " + opponent.getPokemonDetails().statistics().getHp());
        System.out.println("Defense: " + opponent.getCurrentDefenceLvl());
        System.out.println("Speed: " + opponent.getPokemonDetails().statistics().getSpeed());
        System.out.println();
    }

    private PokemonInDuel getFirstAttacker() {
        return opponent.getPokemonDetails().statistics().getSpeed() > myPokemon.getPokemonDetails().statistics().getSpeed()
                ? opponent
                : myPokemon;
    }

}
