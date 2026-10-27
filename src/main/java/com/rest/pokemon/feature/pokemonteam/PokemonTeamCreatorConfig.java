package com.rest.pokemon.feature.pokemonteam;

import com.rest.pokemon.external.pokeapi.PokeApiConnector;
import com.rest.pokemon.repository.PokemonTeamDao;
import com.rest.pokemon.concurrent.VirtualThreadTaskRunner;
import com.rest.pokemon.feature.pokemonteam.idselector.RandomPokemonIdsSelector;
import com.rest.pokemon.mapper.PokemonDtoToPokemonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PokemonTeamCreatorConfig {

    @Bean
    public PokemonTeamCreator pokemonTeamCreator(
            PokeApiConnector pokeApiConnector,
            PokemonTeamDao pokemonTeamDao,
            RandomPokemonIdsSelector randomPokemonTeamSelector,
            VirtualThreadTaskRunner virtualThreadTaskRunner,
            PokemonDtoToPokemonMapper pokemonMapper
    ) {
        return new PokemonTeamCreator(
                randomPokemonTeamSelector,
                pokeApiConnector,
                pokemonMapper,
                pokemonTeamDao,
                virtualThreadTaskRunner
        );
    }


}
