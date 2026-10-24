package com.rest.pokemon.mapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
enum Stat {
    HP("hp"),
    ATTACK("attack"),
    DEFENSE("defense"),
    SPEED("speed");

    private final String key;
}
