package com.rest.pokemon.service.feature.pokemonteam;

import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.repository.PokemonTeam;
import com.rest.pokemon.repository.PokemonTeamDao;
import com.rest.pokemon.service.feature.pokemonteam.idselector.PokemonIdsSelector;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PokemonTeamCreator {

    private final PokemonIdsSelector pokemonIdsSelector;
    private final PokeApiConnector pokeApiConnector;
    private final PokemonTeamDao pokemonTeamDao;

    public Mono<PokemonTeam> create() {
        var ids = Flux.fromIterable(pokemonIdsSelector.selectSixIds());
        return ids
                .flatMap(pokeApiConnector::getPokemonDetails)
                .collectList()
                .map(pokemonTeamDao::create);
    }
}
