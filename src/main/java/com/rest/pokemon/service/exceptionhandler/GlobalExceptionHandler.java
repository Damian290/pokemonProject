package com.rest.pokemon.service.exceptionhandler;

import com.rest.pokemon.service.feature.battle.exception.NoPokemonTeamException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoPokemonTeamException.class)
    public Mono<ResponseEntity<String>> handleNoPokemonTeamException(NoPokemonTeamException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
    }

    @ExceptionHandler(WebClientResponseException.class)
    public Mono<ResponseEntity<String>> handleWebClientResponseException(WebClientResponseException ex) {
        System.out.println("HTTP error during calling api: statusCode: " + ex.getStatusCode());
        System.out.println("Exception: " + ex.getResponseBodyAsString());
        return Mono.just(ResponseEntity.status(ex.getStatusCode()).body("API error: " + ex.getMessage()));
    }

    @ExceptionHandler(WebClientRequestException.class)
    public Mono<ResponseEntity<String>> handleWebClientRequestException(WebClientRequestException ex) {
        System.out.println("Error during sending request to api: " + ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Cannot connect with API. Try again later."));
    }
}
