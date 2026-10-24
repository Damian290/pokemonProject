package com.rest.pokemon.feature.battle.exception;

public class NoPokemonTeamException extends RuntimeException {

    public NoPokemonTeamException(){
        super("You don't have any pokemons! First create a team!");
    }

}
