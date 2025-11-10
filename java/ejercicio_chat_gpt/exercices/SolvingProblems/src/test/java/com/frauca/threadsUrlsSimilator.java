package com.frauca;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class threadsUrlsSimilator {

    public static <T extends Number> void similars(T a, T b) {
        double ratio =  a.doubleValue() / b.doubleValue();
        assertTrue(ratio > 0.8 && ratio < 1.2,
                "Los tiempos deberían ser similares (ratio: " + ratio + ")");
    }

    String downloadUrl(String url) {
        try {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            Thread.sleep(random.nextLong(200, 1000));
            return "Content of " + url;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    String processContent(String content) {
        for (int i = 0; i < 1_000_000; i++) {
            Math.sqrt(i); // Trabajo de CPU
        }
        return "Processed " + content;
    }

    List<String> processWithNormalThreads(List<String> urls) {
        List<CompletableFuture<String>> results = new ArrayList<>();
        for (String url : urls) {
            results.add(CompletableFuture.supplyAsync(()-> downloadUrl(url))
                    .thenApply(this::processContent));
        }
        return results.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    List<String> processWithVirtualThreads(List<String> urls) {
        Executor virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();
        List<CompletableFuture<String>> results = new ArrayList<>();
        for (String url : urls) {
            results.add(CompletableFuture.supplyAsync(()-> downloadUrl(url),virtualThreadExecutor)
                    .thenApply(this::processContent));
        }
        return results.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    List<String> optimalProcess(List<String> urls) {
        Executor virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();
        Executor cpuBoundExecutor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
        List<CompletableFuture<String>> results = new ArrayList<>();
        for (String url : urls) {
            results.add(CompletableFuture.supplyAsync(()-> downloadUrl(url),virtualThreadExecutor)
                    .thenApplyAsync(this::processContent,cpuBoundExecutor));
        }
        return results.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    @Test
    void testPerformance() {
        List<String> urls = IntStream.rangeClosed(1,200)
                .mapToObj(i -> "http://example.com/resource/" + i)
                .toList();
        var normal = Elapsed.run(()-> processWithNormalThreads(urls));
        var virtual = Elapsed.run(()-> processWithVirtualThreads(urls));
        var optimal = Elapsed.run(()-> optimalProcess(urls));
        similars(virtual.timeMillis(),optimal.timeMillis());
        assertTrue(optimal.timeMillis()< normal.timeMillis());
        assertTrue(optimal.memoryBytes()< virtual.memoryBytes());
        assertTrue(optimal.memoryBytes()< normal.memoryBytes());
    }
}
