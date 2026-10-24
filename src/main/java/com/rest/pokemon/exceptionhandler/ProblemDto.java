package com.rest.pokemon.exceptionhandler;

public record ProblemDto(
        String title,
        int status,
        String detail,
        String instance
) {
}
