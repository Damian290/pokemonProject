package com.rest.pokemon.service.feature.battle;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.repository.PokemonTeamDao;
import com.rest.pokemon.service.feature.battle.exception.NoPokemonTeamException;
import com.rest.pokemon.service.feature.battle.onevsone.OneVsOneResolver;
import com.rest.pokemon.service.feature.battle.onevsone.OneVsOneRing;
import com.rest.pokemon.service.feature.battle.oponentfinder.OpponentFinder;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BattleManager {

    private final PokemonTeamDao pokemonTeamDao;
    private final OpponentFinder opponentFinder;
    private final OneVsOneResolver oneVsOneResolver;

    public Mono<PokemonDetails> battle() {
        var myPokemons = pokemonTeamDao.getAll().pokemonDetails();
        if (myPokemons.isEmpty()) {
            throw new NoPokemonTeamException();
        }
        var opponent = opponentFinder.find();

        return opponent.map(opponentPokemon -> {
                    var results = oneVsOneResolver.resolve(myPokemons.getFirst(), opponentPokemon);
                    removeDefeatedPokemon(results);
                    return results;
                })
                .doOnNext(it -> System.out.println("The winner is: " + it.getWinner()))
                .map(oneVsOneRing -> oneVsOneRing.getWinner().getPokemonDetails());
    }

    private void removeDefeatedPokemon(OneVsOneRing results) {
        if (!results.isMyPokemonWinner()) {
            var removedPokemon = pokemonTeamDao.removeFirst();
            System.out.println("Pokemon " + removedPokemon + " removed from your team");
        } else {
            pokemonTeamDao.updateFirstPokemon(results.getMyPokemon().getPokemonDetails());
        }
    }
}
