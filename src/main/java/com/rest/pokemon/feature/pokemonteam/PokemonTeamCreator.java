package com.rest.pokemon.feature.pokemonteam;

import com.rest.pokemon.concurrent.VirtualThreadTaskRunner;
import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.feature.pokemonteam.idselector.PokemonIdsSelector;
import com.rest.pokemon.mapper.PokemonDtoToPokemonMapper;
import com.rest.pokemon.repository.PokemonTeamDao;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class PokemonTeamCreator {
    private final PokemonIdsSelector pokemonIdsSelector;
    private final PokeApiConnector pokeApiConnector;
    private final PokemonDtoToPokemonMapper pokemonMapper;
    private final PokemonTeamDao pokemonTeamDao;
    private final VirtualThreadTaskRunner virtualThreadTaskRunner;

    public PokemonTeam create(String teamName) {
        var pokemonIds = pokemonIdsSelector.selectSixIds();
        var tasks = pokemonIds.stream()
                .<Callable<Pokemon>>map(id ->
                        () -> pokemonMapper.map(pokeApiConnector.getPokemonDetails(id), id))
                .toList();
        var pokemons = virtualThreadTaskRunner.runAll(tasks);
        return pokemonTeamDao.create(teamName, pokemons);
    }
}
