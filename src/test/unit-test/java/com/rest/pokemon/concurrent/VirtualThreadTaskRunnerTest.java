package com.rest.pokemon.concurrent;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VirtualThreadTaskRunnerTest {
    private final VirtualThreadTaskRunner sut = new VirtualThreadTaskRunner();

    @Test
    void shouldReturnResultsInInputOrder() throws Exception {
        //given
        @SuppressWarnings("unchecked")
        Callable<String> first = mock(Callable.class);
        @SuppressWarnings("unchecked")
        Callable<String> second = mock(Callable.class);
        when(first.call()).thenReturn("first");
        when(second.call()).thenReturn("second");

        //when
        var results = sut.runAll(List.of(first, second));

        //then
        assertEquals(List.of("first", "second"), results);
        verify(first).call();
        verify(second).call();
    }

    @Test
    void shouldPropagateRuntimeFailureFromTask() throws Exception {
        //given
        @SuppressWarnings("unchecked")
        Callable<String> task = mock(Callable.class);
        var failure = new IllegalArgumentException("bad task input");
        when(task.call()).thenThrow(failure);

        //when
        var thrown = assertThrows(IllegalArgumentException.class, () -> sut.runAll(List.of(task)));

        //then
        assertSame(failure, thrown);
    }

    @Test
    void shouldWrapCheckedFailureFromTask() throws Exception {
        //given
        @SuppressWarnings("unchecked")
        Callable<String> task = mock(Callable.class);
        var failure = new IOException("task failed");
        when(task.call()).thenThrow(failure);

        //when
        var thrown = assertThrows(IllegalStateException.class, () -> sut.runAll(List.of(task)));

        //then
        assertEquals("A concurrent task failed", thrown.getMessage());
        assertSame(failure, thrown.getCause());
    }
}
