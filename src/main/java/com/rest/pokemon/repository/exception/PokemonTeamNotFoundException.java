package com.rest.pokemon.repository.exception;

public class PokemonTeamNotFoundException extends RuntimeException {
    public PokemonTeamNotFoundException(String identifier) {
        super("Pokemon team not found: " + identifier);
    }
}
