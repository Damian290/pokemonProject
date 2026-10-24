package com.rest.pokemon.external.pokeapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@EnableRetry
public class PokeApiConnectorConfig {
    @Bean
    public PokeApiClient pokeApiClient(
            @Value("${external.poke-api.url}") String apiUrl,
            @Value("${external.timeout.connect}") Duration connectTimeout,
            @Value("${external.timeout.read}") Duration readTimeout) {
        var httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .build();
        var requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout);

        var restClient = RestClient.builder()
                .baseUrl(apiUrl)
                .requestFactory(requestFactory)
                .build();
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build().createClient(PokeApiClient.class);
    }

    @Bean
    public PokeApiConnector pokeApiConnector(PokeApiClient pokeApiClient) {
        return new PokeApiConnector(pokeApiClient);
    }
}
