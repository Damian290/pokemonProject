package com.rest.pokemon.domain.battle;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class PokemonInDuel {
    private final PokemonDetails pokemonDetails;
    long currentDefenceLvl;

    public void setCurrentDefenceLvl(long currentDefenceLvl) {
        this.currentDefenceLvl = Math.max(0, currentDefenceLvl);
    }

    public void setCurrentHp(long currentHp) {
        pokemonDetails.statistics().setHp(Math.max(0, currentHp));
    }
}
