package com.rest.pokemon.feature.battle.opponentfinder;

import com.rest.pokemon.domain.pokemon.Pokemon;
import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.mapper.PokemonDtoToPokemonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class OpponentFinder {

    private final PokeApiConnector pokeApiConnector;
    private final PokemonDtoToPokemonMapper pokemonMapper;
    private final Random random;

    public Pokemon find(List<Pokemon> teamMembers) {
        Set<Integer> teamPokemonIds = teamMembers.stream()
                .map(Pokemon::id)
                .collect(Collectors.toSet());

        int randomId;
        do {
            randomId = random.nextInt(1, 152);
        } while (teamPokemonIds.contains(randomId));

        var opponent = pokemonMapper.map(pokeApiConnector.getPokemonDetails(randomId), randomId);
        log.debug("Opponent selected: pokemonId={}", opponent.id());
        return opponent;
    }

}
