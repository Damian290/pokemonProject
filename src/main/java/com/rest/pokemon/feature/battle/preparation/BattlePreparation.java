package com.rest.pokemon.feature.battle.preparation;

import com.rest.pokemon.domain.battle.BattleParticipants;
import com.rest.pokemon.repository.PokemonTeamDao;
import com.rest.pokemon.feature.battle.exception.NoPokemonTeamException;
import com.rest.pokemon.feature.battle.opponentfinder.OpponentFinder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BattlePreparation {
    private final PokemonTeamDao pokemonTeamDao;
    private final OpponentFinder opponentFinder;

    public BattleParticipants prepare(String teamName) {
        var team = pokemonTeamDao.getByName(teamName);
        var teamId = team.id();
        var teamMembers = team.pokemons();
        if (teamMembers.isEmpty()) {
            throw new NoPokemonTeamException();
        }

        var myPokemon = teamMembers.getFirst();
        var opponentPokemon = opponentFinder.find(teamMembers);
        return new BattleParticipants(teamId, myPokemon, opponentPokemon);
    }
}
