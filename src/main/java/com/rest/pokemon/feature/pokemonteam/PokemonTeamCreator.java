package com.rest.pokemon.feature.pokemonteam;

import com.rest.pokemon.concurrent.VirtualThreadTaskRunner;
import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.feature.pokemonteam.idselector.PokemonIdsSelector;
import com.rest.pokemon.mapper.PokemonDtoToPokemonDetailsMapper;
import com.rest.pokemon.repository.PokemonTeamDao;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class PokemonTeamCreator {
    private final PokemonIdsSelector pokemonIdsSelector;
    private final PokeApiConnector pokeApiConnector;
    private final PokemonDtoToPokemonDetailsMapper pokemonMapper;
    private final PokemonTeamDao pokemonTeamDao;
    private final VirtualThreadTaskRunner virtualThreadTaskRunner;

    public PokemonTeam create(String teamName) {
        var pokemonIds = pokemonIdsSelector.selectSixIds();
        var tasks = pokemonIds.stream()
                .<Callable<PokemonDetails>>map(id ->
                        () -> pokemonMapper.map(pokeApiConnector.getPokemonDetails(id), id))
                .toList();
        var pokemon = virtualThreadTaskRunner.runAll(tasks);
        return pokemonTeamDao.create(teamName, pokemon);
    }
}
