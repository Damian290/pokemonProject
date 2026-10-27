package com.rest.pokemon.domain.pokemon;

import java.util.Objects;

public final class Pokemon {
    private final Integer id;
    private final String name;
    private final Statistics baseStatistics;
    private long currentHpLvl;
    private long currentDefenceLvl;

    public Pokemon(Integer id, String name, Statistics statistics) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.baseStatistics = Objects.requireNonNull(statistics, "statistics");
        if (statistics.hp() <= 0 || statistics.attack() <= 0) {
            throw new IllegalArgumentException("Pokemon HP and attack must be positive");
        }
        this.currentHpLvl = statistics.hp();
        this.currentDefenceLvl = statistics.defence();
    }

    private Pokemon(Integer id, String name, Statistics statistics, long currentHpLvl, long currentDefenceLvl) {
        this(id, name, statistics);
        if (currentHpLvl < 0) {
            throw new IllegalArgumentException("Current HP cannot be negative");
        }
        this.currentHpLvl = currentHpLvl;
        this.currentDefenceLvl = currentDefenceLvl;
    }

    public Integer id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Statistics statistics() {
        return new Statistics(currentHpLvl, baseStatistics.attack(), baseStatistics.defence(), baseStatistics.speed());
    }

    public long getSpeed() {
        return baseStatistics.speed();
    }

    public long getAttack() {
        return baseStatistics.attack();
    }

    public boolean isDead() {
        return currentHpLvl == 0;
    }

    public void receiveDamage(long damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }
        if (currentDefenceLvl > 0) {
            currentDefenceLvl = Math.max(0, currentDefenceLvl - damage);
        } else {
            currentHpLvl = Math.max(0, currentHpLvl - damage);
        }
    }

    public Pokemon copy() {
        return new Pokemon(id, name, baseStatistics, currentHpLvl, currentDefenceLvl);
    }

    public Pokemon afterDuel() {
        if (isDead()) {
            return copy();
        }
        return new Pokemon(id, name, statistics());
    }

}
