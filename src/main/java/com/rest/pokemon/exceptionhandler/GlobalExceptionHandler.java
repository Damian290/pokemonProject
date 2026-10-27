package com.rest.pokemon.exceptionhandler;

import com.rest.pokemon.external.pokeapi.PokeApiPokemonDetailsException;
import com.rest.pokemon.external.pokeapi.PokeApiPokemonListException;
import com.rest.pokemon.feature.battle.exception.NoPokemonTeamException;
import com.rest.pokemon.repository.exception.PokemonTeamNameAlreadyExistsException;
import com.rest.pokemon.repository.exception.PokemonTeamNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoPokemonTeamException.class)
    public ResponseEntity<ProblemDto> handleNoPokemonTeam(
            NoPokemonTeamException exception, HttpServletRequest request) {
        log.error("Cannot start battle because the team has no Pokemon", exception);
        return ResponseEntity.badRequest()
                .body(buildProblemDto(HttpStatus.BAD_REQUEST, exception.getMessage(), request));
    }

    @ExceptionHandler(PokemonTeamNameAlreadyExistsException.class)
    public ResponseEntity<ProblemDto> handleDuplicateTeamName(
            PokemonTeamNameAlreadyExistsException exception, HttpServletRequest request) {
        log.debug("Cannot create Pokemon team because its name is already in use", exception);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildProblemDto(HttpStatus.CONFLICT, "A team with this name already exists.", request));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDto> handleDataIntegrityViolation(
            DataIntegrityViolationException exception, HttpServletRequest request) {
        log.error("Request conflicts with existing data", exception);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildProblemDto(HttpStatus.CONFLICT, "The request conflicts with existing data.", request));
    }

    @ExceptionHandler(PokemonTeamNotFoundException.class)
    public ResponseEntity<ProblemDto> handlePokemonTeamNotFound(
            PokemonTeamNotFoundException exception, HttpServletRequest request) {
        log.error("Pokemon team was not found", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildProblemDto(HttpStatus.NOT_FOUND, exception.getMessage(), request));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            HandlerMethodValidationException.class
    })
    public ResponseEntity<ProblemDto> handleBadRequest(Exception exception, HttpServletRequest request) {
        log.error("Invalid request", exception);
        return ResponseEntity.badRequest()
                .body(buildProblemDto(HttpStatus.BAD_REQUEST, "The request is invalid.", request));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDto> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        log.error("HTTP method is not supported for this endpoint", exception);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(buildProblemDto(HttpStatus.METHOD_NOT_ALLOWED,
                        "The HTTP method is not supported for this endpoint.", request));
    }

    @ExceptionHandler({PokeApiPokemonListException.class, PokeApiPokemonDetailsException.class})
    public ResponseEntity<ProblemDto> handlePokeApiError(RuntimeException exception, HttpServletRequest request) {
        log.error("Cannot communicate with PokeAPI", exception);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(buildProblemDto(HttpStatus.BAD_GATEWAY, "PokeAPI is unavailable.", request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDto> handleUnexpectedError(Exception exception, HttpServletRequest request) {
        log.error("Unexpected application error", exception);
        return ResponseEntity.internalServerError()
                .body(buildProblemDto(HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred.", request));
    }

    private ProblemDto buildProblemDto(HttpStatus status, String detail, HttpServletRequest request) {
        return new ProblemDto(status.getReasonPhrase(), status.value(), detail, request.getRequestURI());
    }
}
