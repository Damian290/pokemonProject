package com.rest.pokemon.config;

import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

@Component("pokeApiRetryPolicy")
public class PokeApiRetryPolicy {

    public boolean isRetryable(Throwable exception) {
        return exception instanceof ResourceAccessException
                || (exception instanceof RestClientResponseException response
                && response.getStatusCode().is5xxServerError());
    }
}
