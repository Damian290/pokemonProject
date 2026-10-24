package com.rest.pokemon.repository.exception;

public class PokemonTeamNameAlreadyExistsException extends RuntimeException {
    public PokemonTeamNameAlreadyExistsException(String teamName) {
        super("Pokemon team name already exists: " + teamName);
    }
}
