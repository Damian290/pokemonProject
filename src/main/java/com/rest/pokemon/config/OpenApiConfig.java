package com.rest.pokemon.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pokemonApiDocumentation() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pokemon Battle API")
                        .version("v1")
                        .description("A simple API for creating a Pokemon team and battling random opponents."));
    }
}
