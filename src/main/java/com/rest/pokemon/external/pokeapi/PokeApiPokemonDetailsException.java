package com.rest.pokemon.external.pokeapi;

public class PokeApiPokemonDetailsException extends RuntimeException {
    public PokeApiPokemonDetailsException(int id, Throwable cause) {
        super("Cannot retrieve Pokemon details for id: " + id, cause);
    }
}
