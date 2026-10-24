package com.rest.pokemon.external.pokeapi;

public class PokeApiPokemonListException extends RuntimeException {
    public PokeApiPokemonListException(int pokemonCount, Throwable cause) {
        super("Cannot retrieve Pokemon list for count: " + pokemonCount, cause);
    }
}
