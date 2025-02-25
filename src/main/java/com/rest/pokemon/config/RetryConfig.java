package com.rest.pokemon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
public class RetryConfig {

    @Bean
    public Retry pokeApiRetrySpec() {
        return Retry.backoff(3, Duration.ofSeconds(1))
                .filter(throwable -> throwable instanceof WebClientRequestException ||
                        (throwable instanceof WebClientResponseException webEx
                                && webEx.getStatusCode().is5xxServerError()));
    }
}
