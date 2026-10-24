package com.rest.pokemon.feature.pokemonteam;

import com.rest.pokemon.domain.pokemon.PokemonTeam;
import com.rest.pokemon.repository.PokemonTeamDao;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PokemonTeamService {

    private final PokemonTeamDao pokemonTeamDao;

    public List<PokemonTeam> getTeams() {
        return pokemonTeamDao.getAll();
    }

    public PokemonTeam getTeam(long teamId) {
        return pokemonTeamDao.getById(teamId);
    }
}
