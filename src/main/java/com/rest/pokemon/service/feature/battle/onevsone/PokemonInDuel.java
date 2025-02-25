package com.rest.pokemon.service.feature.battle.onevsone;

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
        if (currentDefenceLvl > 0) {
            this.currentDefenceLvl = currentDefenceLvl;
        } else {
            this.currentDefenceLvl = 0;
        }
    }
}
