package com.rest.pokemon.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PokeApiRetryPolicyTest {
    private final PokeApiRetryPolicy sut = new PokeApiRetryPolicy();

    @Test
    void shouldRetryOnConnectionFailure() {
        //given
        var exception = new ResourceAccessException("connection timeout");

        //when
        var retryable = sut.isRetryable(exception);

        //then
        assertTrue(retryable);
    }

    @Test
    void shouldRetryOnServerError() {
        //given
        var exception = responseException(503);

        //when
        var retryable = sut.isRetryable(exception);

        //then
        assertTrue(retryable);
    }

    @Test
    void shouldNotRetryOnClientError() {
        //given
        var exception = responseException(404);

        //when
        var retryable = sut.isRetryable(exception);

        //then
        assertFalse(retryable);
    }

    private RestClientResponseException responseException(int status) {
        return new RestClientResponseException("external API response", status, "status",
                HttpHeaders.EMPTY, new byte[0], StandardCharsets.UTF_8);
    }
}
