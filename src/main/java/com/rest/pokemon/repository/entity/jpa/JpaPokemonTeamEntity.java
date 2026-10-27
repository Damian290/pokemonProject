package com.rest.pokemon.repository.entity.jpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pokemon_teams", uniqueConstraints =
        @UniqueConstraint(name = "uk_pokemon_teams_name", columnNames = "team_name"))
public class JpaPokemonTeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name", nullable = false, length = 20)
    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "pokemon_position")
    private List<JpaPokemonEntity> pokemons = new ArrayList<>();

    protected JpaPokemonTeamEntity() {
    }

    public JpaPokemonTeamEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JpaPokemonEntity> getPokemons() {
        return pokemons;
    }

    public void replacePokemons(List<JpaPokemonEntity> newPokemons) {
        pokemons.clear();
        newPokemons.forEach(pokemon -> {
            pokemon.setTeam(this);
            pokemons.add(pokemon);
        });
    }
}
