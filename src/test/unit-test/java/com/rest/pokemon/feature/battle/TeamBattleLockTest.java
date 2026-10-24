package com.rest.pokemon.feature.battle;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TeamBattleLockTest {

    private final TeamBattleLock sut = new TeamBattleLock();

    @Test
    void shouldSerializeOperationsForSameTeam() throws Exception {
        //given
        var firstOperationEntered = new CountDownLatch(1);
        var releaseFirstOperation = new CountDownLatch(1);
        var secondOperationEntered = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            var first = executor.submit(() -> sut.execute("Ash", () -> {
                firstOperationEntered.countDown();
                await(releaseFirstOperation);
                return null;
            }));
            assertTrue(firstOperationEntered.await(1, TimeUnit.SECONDS));

            //when
            var second = executor.submit(() -> sut.execute("Ash", () -> {
                secondOperationEntered.countDown();
                return null;
            }));

            //then
            assertFalse(secondOperationEntered.await(100, TimeUnit.MILLISECONDS));
            releaseFirstOperation.countDown();
            assertTrue(secondOperationEntered.await(1, TimeUnit.SECONDS));
            first.get(1, TimeUnit.SECONDS);
            second.get(1, TimeUnit.SECONDS);
        } finally {
            releaseFirstOperation.countDown();
        }
    }

    @Test
    void shouldAllowOperationsForDifferentTeamsToProceedIndependently() throws Exception {
        //given
        var firstOperationEntered = new CountDownLatch(1);
        var releaseFirstOperation = new CountDownLatch(1);
        var secondOperationEntered = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            var first = executor.submit(() -> sut.execute("Ash", () -> {
                firstOperationEntered.countDown();
                await(releaseFirstOperation);
                return null;
            }));
            assertTrue(firstOperationEntered.await(1, TimeUnit.SECONDS));

            //when
            var second = executor.submit(() -> sut.execute("Misty", () -> {
                secondOperationEntered.countDown();
                return null;
            }));

            //then
            assertTrue(secondOperationEntered.await(1, TimeUnit.SECONDS));
            releaseFirstOperation.countDown();
            first.get(1, TimeUnit.SECONDS);
            second.get(1, TimeUnit.SECONDS);
        } finally {
            releaseFirstOperation.countDown();
        }
    }

    private static void await(CountDownLatch latch) {
        try {
            if (!latch.await(1, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Timed out waiting for test latch");
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for test latch", exception);
        }
    }
}
