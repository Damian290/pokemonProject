package com.rest.pokemon.feature.battle;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class TeamBattleLock {

    private final ConcurrentHashMap<String, ReentrantLock> locksByTeamName = new ConcurrentHashMap<>();

    public <T> T execute(String teamName, LockedOperation<T> operation) {
        var lock = locksByTeamName.computeIfAbsent(teamName, ignored -> new ReentrantLock());
        lock.lock();
        try {
            return operation.run();
        } finally {
            lock.unlock();
        }
    }

    @FunctionalInterface
    public interface LockedOperation<T> {
        T run();
    }
}
