package com.rest.pokemon.external.pokeapi;

import com.rest.pokemon.service.mapper.PokemonDtoToPokemonDetailsMapper;
import com.rest.pokemon.service.mapper.PokemonOverviewDtoToPokemonOverviewMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
@Configuration
public class PokeApiConnectorConfig {

    @Bean
    public PokeApiClient pokeApiClient(
            @Value("${external.poke-api.url}") String apiUrl
    ) {
        var webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .filter(logRequest())
                .filter(logResponse())
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10 MB
                .build();
        var httpServiceProxyFactory = HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(WebClientAdapter.create(webClient))
                .build();
        return httpServiceProxyFactory.createClient(PokeApiClient.class);
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request: " + clientRequest.method() + " " + clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            System.out.println("Response status: " + clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

    @Bean
    public PokeApiConnector pokeApiConnector(
            PokeApiClient pokeApiClient,
            Retry retry
    ) {
        return new PokeApiConnector(
                pokeApiClient,
                new PokemonOverviewDtoToPokemonOverviewMapper(),
                new PokemonDtoToPokemonDetailsMapper(),
                retry
        );
    }
}
