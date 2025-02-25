package com.rest.pokemon.service.feature.pokemonteam;

import com.rest.pokemon.repository.PokemonTeam;
import com.rest.pokemon.repository.PokemonTeamDao;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PokemonTeamService {

    private final PokemonTeamDao pokemonTeamDao;

    public Mono<PokemonTeam> getTeam(){
        return Mono.just(pokemonTeamDao.getAll());
    }
}
