package com.rest.pokemon.concurrent;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Component
public class VirtualThreadTaskRunner {

    public <T> List<T> runAll(List<Callable<T>> tasks) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = executor.invokeAll(tasks);
            return futures.stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (ExecutionException exception) {
                            var cause = exception.getCause();
                            if (cause instanceof RuntimeException runtimeException) {
                                throw runtimeException;
                            }
                            if (cause instanceof Error error) {
                                throw error;
                            }
                            throw new IllegalStateException("A concurrent task failed", cause);
                        } catch (InterruptedException exception) {
                            Thread.currentThread().interrupt();
                            throw new IllegalStateException("Interrupted while waiting for concurrent tasks", exception);
                        }
                    })
                    .toList();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for concurrent tasks", exception);
        }
    }
}
