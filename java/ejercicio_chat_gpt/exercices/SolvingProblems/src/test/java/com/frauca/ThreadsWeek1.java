package com.frauca;

import jakarta.annotation.security.RunAs;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static javax.swing.text.html.HTML.Attribute.N;
import static org.junit.jupiter.api.Assertions.*;

public class ThreadsWeek1 {
    @Test
    void printThreadName() throws InterruptedException {
        var t1 = new Thread(()->{
            System.out.println(Thread.currentThread().getName());
        });
        t1.start();
        while (t1.isAlive()){
            TimeUnit.MILLISECONDS.sleep(10);
        }
        var sinc = "hola";
    }

    @Test
    void sameWithRunnable(){
        Runnable t1 = ()->{
            System.out.println(Thread.currentThread().getName());
        };
        new Thread(t1).start();
    }
    Runnable printN(int n){
        return printN(n,0);
    }
    Runnable printN(int n,int sleep){
        return () -> {
            var name = Thread.currentThread().getName();
            for(int i=0;i<n;i++){
                System.out.println("%s iteration %s".formatted(name,i));
                if (sleep>0){
                    try {
                        TimeUnit.MILLISECONDS.sleep(sleep);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
    }
    @Test
    void twoThreads() throws InterruptedException {
        Runnable t1 = printN(100);
        var e1 = new Thread(t1);
        var e2 = new Thread(t1);
        e1.join();
        e2.join();
    }

    @Test
    void twoThreadsWaiting() throws InterruptedException {
        Runnable r1 = printN(100);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r1);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    static final class Counter{
        public int value = 0;
        void increment(){
            value = value +1;
        }
    }

    @Test
    void unsyncCounter() throws InterruptedException {

        final Counter counter =new Counter();
        Runnable r1 = ()->{
            for(int i=0;i<1000;i++){
                counter.increment();
            }
        };

        var t1 = new Thread(r1);
        var t2 = new Thread(r1);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        assertNotEquals(2000,counter.value);
    }

    @Test
    void syncCounter() throws InterruptedException {

        final Counter counter =new Counter();
        Runnable r1 = ()->{
            for(int i=0;i<1000;i++){
                synchronized (counter) {
                    counter.increment();
                }
            }
        };

        var t1 = new Thread(r1);
        var t2 = new Thread(r1);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        assertEquals(2000,counter.value);
    }

    private static volatile boolean cond = true;
    @Test
    void valatileTest() throws InterruptedException {
        var t1 = new Thread(()->{
            while (cond){
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.start();

        assertTrue(t1.isAlive());
        cond = false;
        TimeUnit.MILLISECONDS.sleep(3);

        assertFalse(t1.isAlive());
    }

    @Test
    void consumerProducer() throws InterruptedException {
        final LinkedList<Integer> buffer = new LinkedList<>();
        final int LIMIT = 5;

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 30; i++) {
                synchronized (buffer) {
                    while (buffer.size() == LIMIT) { // espera si está lleno
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    buffer.add(i);
                    buffer.notifyAll();
                }
            }
            synchronized (buffer) {
                buffer.add(-1);
                buffer.notifyAll();
            }
        });

        Thread consumer = new Thread(() -> {
            while (true) {
                int value;
                synchronized (buffer) {
                    while (buffer.isEmpty()) {
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    value = buffer.removeFirst();
                    buffer.notifyAll();
                }
                if (value < 0) break;
                System.out.println("Consumed " + value);
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    @Test
    void reentrantLock() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        int[] counter = {0};

        Runnable increment = () -> {
            while (true) {
                lock.lock();
                try {
                    if (counter[0] >= 100) {
                        break;
                    }
                    counter[0]++;
                    System.out.println(Thread.currentThread().getName() + " -> " + counter[0]);
                } finally {
                    lock.unlock();
                }
            }
        };

        Thread t1 = new Thread(increment);
        Thread t2 = new Thread(increment);
        Thread t3 = new Thread(increment);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        assertEquals(100, counter[0]);
    }

    @Test
    void consumerProducerWithConcurrent() throws InterruptedException {
        final BlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(5);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 30; i++) {
                    buffer.put(i); // se bloquea si está lleno
                }
                buffer.put(-1); // señal de fin
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    int value = buffer.take(); // se bloquea si está vacío
                    if (value < 0) break;
                    System.out.println("Consumed " + value);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    @Test
    void mapInParalel() throws InterruptedException {
        var words = new ConcurrentLinkedQueue<>(List.of("java", "kotlin", "java", "scala", "java", "kotlin"));
        var wordsCount = new ConcurrentHashMap<String,Integer>();
        Runnable wordCounter = ()->{
            while (true){
                var word = words.poll();
                if(word == null){
                    break;
                }else{
                    wordsCount.compute(word, (key, value) -> value == null ? 1 : value + 1);
                }
            }
        };
        var t1 = new Thread(wordCounter);
        var t2 = new Thread(wordCounter);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertEquals(wordsCount, Map.of("java",3,"kotlin",2,"scala",1));
    }

    @Test
    void runWithExecutorService() throws InterruptedException {
        Runnable printMe = ()->{
            System.out.println("Hello from thread %s".formatted(Thread.currentThread().getName()));
        };
        var executor = Executors.newFixedThreadPool(3);
        var prints = IntStream.rangeClosed(0,10)
                .mapToObj(i->executor.submit(printMe))
                .toList();
        executor.shutdown();
        executor.awaitTermination(10,TimeUnit.SECONDS);

    }

    @Test
    void mapInParallel2() throws InterruptedException {
        var words = List.of("java", "kotlin", "java", "scala", "java", "kotlin");
        var counts = words.parallelStream()
                .collect(Collectors.toConcurrentMap(
                        w -> w,
                        w -> 1,
                        Integer::sum
                ));

        assertEquals(Map.of("java", 3, "kotlin", 2, "scala", 1), counts);
    }

    @Test
    void sumOneMillion(){
        var result = IntStream.rangeClosed(1,1_000_000)
                .parallel()
                .sum();

        assertEquals(1784293664,result);
    }

    @Test
    void sumOneMillionES() throws ExecutionException, InterruptedException {
        try(var executor = Executors.newFixedThreadPool(12)) {

            Future<Integer> res = executor.submit(() -> {
                var result = 0;
                for (int i = 1; i <= 1_000_000; i++) {
                    result += i;
                }
                return result;
            });

            assertEquals(1784293664, res.get());
        }
    }

    @Test
    void sumOneMillionWithFuture() throws ExecutionException, InterruptedException {
        try (var executor = Executors.newFixedThreadPool(4)) {
            Callable<Integer> task = () -> IntStream.rangeClosed(1, 1_000_000).sum();
            Future<Integer> result = executor.submit(task);
            assertEquals(1784293664, result.get());
        }
    }

    @Test
    void showYourName() throws ExecutionException, InterruptedException {
        try(var executor = Executors.newFixedThreadPool(12)) {
            Callable<String> showYou = ()->Thread.currentThread().getName();
            var t1 = executor.submit(showYou);
            var t2 = executor.submit(showYou);
            var t3 = executor.submit(showYou);

            assertEquals("pool-1-thread-1",t1.get());
            assertEquals("pool-1-thread-2",t2.get());
            assertEquals("pool-1-thread-3",t3.get());
        }
    }

    @Test
    void showThreadNames() throws ExecutionException, InterruptedException {
        try (var executor = Executors.newFixedThreadPool(3)) {
            Callable<String> task = () -> Thread.currentThread().getName();

            Future<String> f1 = executor.submit(task);
            Future<String> f2 = executor.submit(task);
            Future<String> f3 = executor.submit(task);

            Set<String> threadNames = Set.of(f1.get(), f2.get(), f3.get());

            // ✅ Validamos que son 3 hilos distintos, sin depender del nombre exacto
            assertEquals(3, threadNames.size());
        }
    }

    @Test
    void executionCompletionService() throws ExecutionException, InterruptedException {
        try(var executor = Executors.newFixedThreadPool(3)) {
            var completion = new ExecutorCompletionService<Integer>(executor);
            Callable<Integer> r = ()->{
                var time = new Random().nextInt(500)+10;
                TimeUnit.MILLISECONDS.sleep(time);
                return time;
            };
            completion.submit(r);
            completion.submit(r);
            completion.submit(r);
            for (int i = 0; i < 3; i++) {
                System.out.println(completion.take().get());
            }

        }
    }

    @Test
    void completionServiceExample() throws InterruptedException, ExecutionException {
        try (var executor = Executors.newFixedThreadPool(3)) {
            var completion = new ExecutorCompletionService<Integer>(executor);

            Callable<Integer> task = () -> {
                int time = ThreadLocalRandom.current().nextInt(100, 500);
                TimeUnit.MILLISECONDS.sleep(time);
                return time;
            };

            for (int i = 0; i < 5; i++) {
                completion.submit(task);
            }

            for (int i = 0; i < 5; i++) {
                Integer result = completion.take().get();
                System.out.println("Task finished in " + result + " ms");
            }
        }
    }

    @Test
    void coundDown() throws InterruptedException {
        var countDown = new CountDownLatch(3);
        Callable<Integer> r = ()->{
            var time = new Random().nextInt(500)+10;
            TimeUnit.MILLISECONDS.sleep(time);
            countDown.countDown();
            return time;
        };
        var executor = Executors.newFixedThreadPool(3);

        var db = executor.submit(r);
        var cache = executor.submit(r);
        var server = executor.submit(r);

        countDown.await();
        assertTrue(db.isDone());
        assertTrue(cache.isDone());
        assertTrue(server.isDone());
    }

    @Test
    void bearer() throws InterruptedException {
        var barrier = new CyclicBarrier(4);
        Callable<Integer> r = ()->{
            System.out.println(Thread.currentThread().getName()+ " preparing");
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(200));
            barrier.await();
            System.out.println(Thread.currentThread().getName()+ " running");
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(200));
            System.out.println(Thread.currentThread().getName()+ " finished");
            return 0;
        };
        var executor = Executors.newFixedThreadPool(4);
        List.of(
                executor.submit(r),
                executor.submit(r),
                executor.submit(r),
                executor.submit(r)
        );
        executor.shutdown();
        executor.awaitTermination(1000,TimeUnit.MILLISECONDS);
    }

    @Test
    void completableFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<String> f = CompletableFuture.supplyAsync(()->"Hello World");
        assertEquals("Hello World",f.get());
    }

    @Test
    void completableFutureChain() throws ExecutionException, InterruptedException {
        CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> "Hello")
                .thenApply(s -> s + " World")
                .thenApply(s -> s + " from CompletableFuture");
        assertEquals("Hello World from CompletableFuture", f.get());
    }

    @Test
    void completableFutureCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "World");

        CompletableFuture<String> combined = f1.thenCombine(f2, (s1, s2) -> s1 + " " + s2);

        assertEquals("Hello World", combined.get());
    }

    CompletableFuture<Integer>  provideFuture() {
        return CompletableFuture.supplyAsync(() -> {
            if (new Random().nextBoolean()) {
                throw new RuntimeException("Random failure");
            }
            return 42;
        }).exceptionally(ex -> {
            System.out.println("Caught exception: " + ex.getMessage());
            return -1;
        });
    }

    @Test
    void completableFutureErrorHandling() {
        List<CompletableFuture<Integer>> futures = IntStream.range(0, 8).mapToObj(i -> provideFuture()).toList();
        List<Integer> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        assertTrue(results.stream().allMatch(r -> r == 42 || r == -1));
        assertTrue(results.contains(42));
        assertTrue(results.contains(-1));
    }

    @Test
    void completableFutureCompose() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> f = CompletableFuture.supplyAsync(() -> 5)
                .thenCompose(x -> CompletableFuture.supplyAsync(() -> x * 2));
        assertEquals(10, f.get());
    }

    @Test
    void completableFutureAllOf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "World");
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "from CompletableFuture");

        CompletableFuture<Void> allOf = CompletableFuture.allOf(f1, f2, f3);

        CompletableFuture<List<String>> allResults = allOf.thenApply(v ->
                List.of(f1.join(), f2.join(), f3.join())
        );

        List<String> results = allResults.get();
        assertEquals(List.of("Hello", "World", "from CompletableFuture"), results);
    }



    @Test
    void threeSources(){
        CompletableFuture<Integer> db1 = CompletableFuture.supplyAsync(()->1);
        CompletableFuture<Integer> db2 = CompletableFuture.supplyAsync(()->2);
        CompletableFuture<Integer> db3 = CompletableFuture.supplyAsync(()->3);

        Integer result = CompletableFuture.allOf(db1,db2,db3)
                .thenApply(v-> db1.join() + db2.join() + db3.join())
                .join();
        Integer result2 = db1.thenCombine(db2, Integer::sum)
                .thenCombine(db3, Integer::sum)
                .join();
        assertEquals(6,result);
        assertEquals(result,result2);
    }

    @Test
    void composeAndHandle(){
        CompletableFuture<Integer> f = CompletableFuture.supplyAsync(()->5)
                .thenCompose(x->CompletableFuture.supplyAsync(()->x*2))
                .handle((x,e)-> e==null ? x : -1) //exceptionally is the same if we do not process the exception
                .exceptionally(e->-1); // handle would be needed if we want to process the exception
        assertEquals(10,f.join());
    }

    @Test
    void timeOut(){
        CompletableFuture<String> f = CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Hello";
        }).orTimeout(50,TimeUnit.MILLISECONDS);

        String result = f.exceptionally(ex -> "Timeout occurred").join();

        assertEquals("Timeout occurred", result);
    }

    @Test
    void firstToComplete() throws ExecutionException, InterruptedException {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Result from f1";
        });

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Result from f2";
        });

        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Result from f3";
        });

        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(f1, f2, f3);

        String firstResult = anyOf.thenApply(String.class::cast).join();
        assertEquals("Result from f2", firstResult);
    }

    @Test
    void streamOfFutures() throws ExecutionException, InterruptedException {
        List<CompletableFuture<Integer>> futures = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> i * 2))
                .toList();

        CompletableFuture<List<Integer>> allResults = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        ).thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );

        List<Integer> results = allResults.get();
        assertEquals(List.of(2, 4, 6, 8, 10), results);
    }

    private static long sumSequential() {
        long sum = 0;
        for (int i = 1; i <= 10_000_000; i++) {
            sum += i;
        }
        return sum;
    }

    private static long sumParallel() {
        return LongStream.rangeClosed(1, 10_000_000)
                .parallel()
                .sum();
    }

    // Virtual threads summation
    private static long sumVirtualThreads() throws Exception {
        int numThreads = Runtime.getRuntime().availableProcessors();
        int chunkSize = 10_000_000 / numThreads;

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        AtomicLong totalSum = new AtomicLong(0);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            final int start = i * chunkSize + 1;
            final int end = (i == numThreads - 1) ? 10_000_000 : (i + 1) * chunkSize;

            Future<?> future = executor.submit(() -> {
                long partialSum = 0;
                for (int j = start; j <= end; j++) {
                    partialSum += j;
                }
                totalSum.addAndGet(partialSum);
            });
            futures.add(future);
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            future.get();
        }

        executor.shutdown();
        return totalSum.get();
    }

    @Test
    void sumTenMillionsInparallel() throws Exception {
        long start = System.nanoTime();
        long sum = sumSequential();
        long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println("Elapsed: " + elapsed + " milli seconds");
        assertEquals(50000005000000L,sum);
        start = System.nanoTime();
        sum = sumParallel();
        elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println("Elapsed: " + elapsed + " milli seconds");
        assertEquals(50000005000000L,sum);
        sum = sumVirtualThreads();
        elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println("Elapsed: " + elapsed + " milli seconds");
        assertEquals(50000005000000L,sum);
    }

    String randomWord(){
        return RandomStringUtils.randomAlphabetic(new Random().nextInt(20)+1).toLowerCase();
    }

    @Test
    void thoundsRandomWords(){
        Set<String> words = ConcurrentHashMap.newKeySet();

        IntStream.range(0, 10000) // Generate more than needed
                .parallel()
                .mapToObj(i -> randomWord())
                .forEach(words::add);

    }

    @Test
    void processUrls()  {
        ConcurrentLinkedQueue urls = new ConcurrentLinkedQueue<>(List.of(
                "http://example.com/page1",
                "http://example.com/page2",
                "http://example.com/page3",
                "http://example.com/page4",
                "http://example.com/page5"
        ));
        urls.parallelStream()
                .map(url -> {
                    try {
                        long start = System.currentTimeMillis();
                        // Simulate processing time
                        TimeUnit.MILLISECONDS.sleep(new Random().nextInt(500) + 100);
                        long end = System.currentTimeMillis();
                        return end - start;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return -1L;
                    }
                })
                .collect(Collectors.toList());
    }

    @Test
    void processingRequests() throws InterruptedException {
        AtomicInteger activeRequests = new AtomicInteger(0);
        AtomicInteger maxConcurrent = new AtomicInteger(0);

        System.out.println("Iniciando 100 peticiones concurrentes...\n");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 1; i <= 100; i++) {
                final int requestId = i;

                executor.submit(() -> {
                    // Registrar inicio
                    int active = activeRequests.incrementAndGet();
                    maxConcurrent.updateAndGet(max -> Math.max(max, active));

                    System.out.println("Request " + requestId + " iniciada (activas: " + active + ")");

                    try {
                        // Simular procesamiento (10-200 ms)
                        int delay = ThreadLocalRandom.current().nextInt(10, 201);
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    // Registrar fin
                    activeRequests.decrementAndGet();
                    System.out.println("Request " + requestId + " finalizada");
                });
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
        }
    }
}
