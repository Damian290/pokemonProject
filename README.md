# PROJECT DESCRIPTION
This application interacts with an external API:
* [Poke Api](http://pokeapi.co/docs/v2)

The goal is to create a draft of a game where you and your Pokémon team battle against opponent Pokémon.

# Battle rules

* A winning team Pokémon keeps its remaining HP for the next battle, while its battle defence resets to its base defence at the start of each duel. This is an intentional game design choice. For example, a Pokémon with 100 HP and 30 base defence that wins with 60 HP and 0 battle defence starts its next duel with 60 HP and 30 battle defence. Only the temporary defence in `Pokemon` is depleted; the base defence remains unchanged.

# Technical details
The project uses a synchronous RestClient for communication with external APIs.
Built with Spring MVC and virtual threads enabled for HTTP request handling.
Written in Java 21.
Swagger UI: http://localhost:8080/swagger-ui.html (after starting the application).
OpenAPI JSON: http://localhost:8080/v3/api-docs.
Prometheus metrics: http://localhost:8080/actuator/prometheus.
Prometheus metrics: http://localhost:8080/actuator/prometheus.
Note: Due to time constraints, the project has limited functionality.

# SideNote
Run unit and component tests: `./gradlew check`.
Run integration tests against the external PokeAPI: `./gradlew integrationTest`.
