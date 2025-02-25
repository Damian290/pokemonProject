package com.rest.pokemon.domain.pokemon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Statistics {
    private long hp;
    private long attack;
    private long defence;
    private long speed;
}
