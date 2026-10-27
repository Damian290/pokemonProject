package com.rest.pokemon.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import com.rest.pokemon.repository.PokemonTeamDao;
import org.springframework.stereotype.Service;

@Service
public class MetricService {
    private final Counter defeatedOpponentsCounter;

    public MetricService(MeterRegistry meterRegistry, PokemonTeamDao pokemonTeamDao) {
        this.defeatedOpponentsCounter = meterRegistry.counter("pokemon.battles.opponents.defeated");
        Gauge.builder("pokemon.teams.pokemon.count", pokemonTeamDao,
                        dao -> dao.getAll().stream().mapToInt(team -> team.pokemons().size()).sum())
                .description("Total number of Pokemon across all teams")
                .register(meterRegistry);
    }

    public void incrementDefeatedOpponents() {
        defeatedOpponentsCounter.increment();
    }
}
