package com.rest.pokemon.domain.pokemon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public record PokemonTeam(
        long id,
        String name,
        List<Pokemon> pokemons
) {
    public PokemonTeam {
        if (id <= 0) {
            throw new IllegalArgumentException("Team ID must be positive");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Team name must not be blank");
        }
        Objects.requireNonNull(pokemons, "pokemons");

        var pokemonIds = new HashSet<Integer>();
        for (var pokemon : pokemons) {
            Objects.requireNonNull(pokemon, "Pokemon team cannot contain null");
            if (!pokemonIds.add(pokemon.id())) {
                throw new IllegalArgumentException("Duplicate Pokemon ID in team: " + pokemon.id());
            }
        }
        pokemons = pokemons.stream().map(Pokemon::copy).toList();
    }

    @Override
    public List<Pokemon> pokemons() {
        return pokemons.stream().map(Pokemon::copy).toList();
    }

    public PokemonTeam removeDefeatedPokemon(Pokemon defeatedPokemon) {
        var updatedPokemons = new ArrayList<>(pokemons);
        updatedPokemons.remove(findPokemonIndex(defeatedPokemon));
        return new PokemonTeam(id, name, updatedPokemons);
    }

    public PokemonTeam updatePokemonState(Pokemon pokemon) {
        var updatedPokemons = new ArrayList<>(pokemons);
        updatedPokemons.set(findPokemonIndex(pokemon), pokemon);
        return new PokemonTeam(id, name, updatedPokemons);
    }

    private int findPokemonIndex(Pokemon pokemon) {
        Objects.requireNonNull(pokemon, "pokemon");
        for (int index = 0; index < pokemons.size(); index++) {
            if (pokemons.get(index).id().equals(pokemon.id())) {
                return index;
            }
        }
        throw new IllegalArgumentException("Pokemon is not in this team: " + pokemon.id());
    }
}
