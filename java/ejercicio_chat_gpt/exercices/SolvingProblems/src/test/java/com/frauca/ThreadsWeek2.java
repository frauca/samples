package com.frauca;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadsWeek2 {
    static Runtime runtime = Runtime.getRuntime();
    record Elapsed(long timeMillis, long memoryBytes){}

    static Elapsed elapsed(Runnable task) {
        System.gc();
        long start = System.currentTimeMillis();
        long initial = runtime.totalMemory() - runtime.freeMemory();
        task.run();
        long end = System.currentTimeMillis();
        long ending = runtime.totalMemory() - runtime.freeMemory();
        return new Elapsed(end - start, ending - initial);
    }

    @Test
    void sleepersThreads() throws InterruptedException {

        final ExecutorService executors = Executors.newFixedThreadPool(200);
        var normal = elapsed(() -> {
            for (int i = 0; i < 1000; i++) {
                final int threadNumber = i;
                executors.submit(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            executors.shutdown();
            try {
                executors.awaitTermination(10, java.util.concurrent.TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        final ExecutorService virtualExecutors = Executors.newVirtualThreadPerTaskExecutor();
        Elapsed virtual = elapsed(()-> {
            for (int i = 0; i < 1000; i++) {
                final int threadNumber = i;
                virtualExecutors.submit(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            virtualExecutors.shutdown();
            try {
                virtualExecutors.awaitTermination(10, java.util.concurrent.TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        assertTrue(virtual.memoryBytes <= normal.memoryBytes);
        assertTrue(virtual.timeMillis <= normal.timeMillis);

    }

    record ApiResult(int code, Date timestamp){}

    class ApiSimulator{
        AtomicInteger counter = new AtomicInteger(0);
        ApiResult callApi(){
            ThreadLocalRandom random = ThreadLocalRandom.current();
            long waitTime = random.nextLong(500,1000);
            try {
                TimeUnit.MILLISECONDS.sleep(waitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return new ApiResult(counter.incrementAndGet(),new Date());
        }
    }

    @Test
    void virtualThreadsApiCalls() throws InterruptedException {
        ApiSimulator apiSimulator = new ApiSimulator();
        final ExecutorService executors = Executors.newFixedThreadPool(50);
        final List<Future<ApiResult>> futures = new ArrayList<>();
        Elapsed normal = elapsed(()->{
            for(int i=0;i<100;i++){
                futures.add(executors.submit(()-> apiSimulator.callApi()));
            }
            executors.shutdown();
            try {
                executors.awaitTermination(30, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        executors.shutdown();
        executors.awaitTermination(30, TimeUnit.MINUTES);
        futures.clear();
        final ExecutorService virtualExecutors = Executors.newVirtualThreadPerTaskExecutor();
        Elapsed virtual = elapsed(()->{
            for(int i=0;i<100;i++){
                futures.add(virtualExecutors.submit(()->apiSimulator.callApi()));
            }
            virtualExecutors.shutdown();
            try {
                virtualExecutors.awaitTermination(30, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        virtualExecutors.shutdown();
        virtualExecutors.awaitTermination(30, TimeUnit.MINUTES);
        assertTrue(virtual.timeMillis<=normal.timeMillis);
    }

    String downloadField(String url) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long waitTime = random.nextLong(1, 10);
        try {
            TimeUnit.SECONDS.sleep(waitTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Contents of " + url;
    }

    @Test
    void fiveHundredDownloads() throws InterruptedException {
        ApiSimulator apiSimulator = new ApiSimulator();
        final ExecutorService executors = Executors.newFixedThreadPool(50);
        final AtomicInteger counter = new AtomicInteger(0);
        final AtomicInteger maxCounter = new AtomicInteger(0);
        Elapsed normal = elapsed(()->{
            for(int i=0;i<100;i++){
               executors.submit(()->{
                   var currentTrheads = counter.incrementAndGet();
                   maxCounter.updateAndGet(x->Math.max(x,currentTrheads));
                   downloadField("http://example.com/data/");
                   counter.decrementAndGet();
               });
            }
            executors.shutdown();
            try {
                executors.awaitTermination(30, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        executors.shutdown();
        executors.awaitTermination(30, TimeUnit.MINUTES);
        var maxNormal = maxCounter.get();
        counter.set(0);
        maxCounter.set(0);
        final ExecutorService virtualExecutors = Executors.newVirtualThreadPerTaskExecutor();
        Elapsed virtual = elapsed(()->{
            for(int i=0;i<100;i++){
                virtualExecutors.submit(()->{
                    var currentTrheads = counter.incrementAndGet();
                    maxCounter.updateAndGet(x->Math.max(x,currentTrheads));
                    downloadField("http://example.com/data/");
                    counter.decrementAndGet();
                });
            }
            virtualExecutors.shutdown();
            try {
                virtualExecutors.awaitTermination(30, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        virtualExecutors.shutdown();
        var maxVirtual = maxCounter.get();
        virtualExecutors.awaitTermination(30, TimeUnit.MINUTES);
        assertTrue(maxVirtual>=maxNormal);
    }
}
