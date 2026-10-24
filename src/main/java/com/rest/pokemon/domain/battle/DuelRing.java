package com.rest.pokemon.domain.battle;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import lombok.Data;

@Data
public class DuelRing {

    public DuelRing(PokemonDetails myPokemonDetails, PokemonDetails opponent) {
        validatePokemon(myPokemonDetails);
        validatePokemon(opponent);

        this.myPokemon = new PokemonInDuel(myPokemonDetails, myPokemonDetails.statistics().getDefence());
        this.opponent = new PokemonInDuel(opponent, opponent.statistics().getDefence());
        this.currentTurn = getFirstAttacker();
    }

    private final PokemonInDuel myPokemon;
    private final PokemonInDuel opponent;
    private PokemonInDuel currentTurn;

    public PokemonInDuel damageReceiver() {
        return currentTurn == myPokemon ? opponent : myPokemon;
    }

    public boolean isDuelFinished() {
        return myPokemon.getPokemonDetails().statistics().getHp() <= 0
                || opponent.getPokemonDetails().statistics().getHp() <= 0;
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
        return getWinner() == myPokemon;
    }

    private PokemonInDuel getFirstAttacker() {
        return opponent.getPokemonDetails().statistics().getSpeed() > myPokemon.getPokemonDetails().statistics().getSpeed()
                ? opponent
                : myPokemon;
    }

    private void validatePokemon(PokemonDetails pokemon) {
        if (pokemon == null || pokemon.statistics() == null
                || pokemon.statistics().getHp() <= 0 || pokemon.statistics().getAttack() <= 0) {
            var pokemonName = pokemon == null ? "unknown" : pokemon.name();
            throw new IllegalStateException(
                    "Pokemon must have statistics with positive HP and attack to enter a duel: " + pokemonName);
        }
    }

}
