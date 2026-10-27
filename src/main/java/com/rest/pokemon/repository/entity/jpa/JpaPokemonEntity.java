package com.rest.pokemon.repository.entity.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "team_pokemons")
public class JpaPokemonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pokemon_id", nullable = false)
    private Integer pokemonId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long hp;

    @Column(nullable = false)
    private long attack;

    @Column(nullable = false)
    private long defence;

    @Column(nullable = false)
    private long speed;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private JpaPokemonTeamEntity team;

    protected JpaPokemonEntity() {
    }

    public JpaPokemonEntity(Integer pokemonId, String name, long hp, long attack, long defence, long speed) {
        this.pokemonId = pokemonId;
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defence = defence;
        this.speed = speed;
    }

    public Integer getPokemonId() {
        return pokemonId;
    }

    public String getName() {
        return name;
    }

    public long getHp() {
        return hp;
    }

    public long getAttack() {
        return attack;
    }

    public long getDefence() {
        return defence;
    }

    public long getSpeed() {
        return speed;
    }

    void setTeam(JpaPokemonTeamEntity team) {
        this.team = team;
    }
}
