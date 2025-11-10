package com.frauca;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.frauca.ThreadsWeek2.elapsed;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadsPoolSimulator {

    class Database {
        private final Semaphore connections = new Semaphore(10, false);
        private final AtomicInteger minAvailable = new AtomicInteger(10); // Inicializar en 10

        public List<String> query(String sql) {
            try {
                connections.acquire();

                // Registrar el mínimo de conexiones disponibles
                int available = connections.availablePermits();
                minAvailable.updateAndGet(min -> Math.min(min, available));

                // Simular query
                ThreadLocalRandom random = ThreadLocalRandom.current();
                Thread.sleep(random.nextLong(50, 200));

                return List.of("record1", "record2", "record3");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } finally {
                connections.release();
            }
        }

        public int getMinAvailable() {
            return minAvailable.get();
        }

        public void resetMinAvailable() {
            minAvailable.set(10);
        }
    }

    @Test
    void runThousandQueries() throws InterruptedException {
        List<String> queries = IntStream.rangeClosed(1, 1000)
                .mapToObj(i -> "SELECT * FROM table WHERE id = " + i)
                .toList();

        System.out.println("🧪 TEST: Database Connection Pool Simulator");
        System.out.println("=".repeat(60));
        System.out.println("Queries: " + queries.size());
        System.out.println("Pool size: 10 conexiones");
        System.out.println();

        // Test 1: Platform Threads
        Database database1 = new Database();
        ExecutorService platformExecutor = Executors.newFixedThreadPool(100);

        var normalElapsed = elapsed(() -> {
            for (String sql : queries) {
                platformExecutor.submit(() -> database1.query(sql));
            }
            platformExecutor.shutdown();
            try {
                platformExecutor.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        int normalMinConnections = database1.getMinAvailable();

        System.out.println("📊 Platform Threads (pool=100):");
        System.out.println("  Tiempo: " + normalElapsed.timeMillis() + " ms");
        System.out.println("  Mínimo conexiones disponibles: " + normalMinConnections);
        System.out.println("  Conexiones usadas (máx): " + (10 - normalMinConnections));

        // Test 2: Virtual Threads
        Database database2 = new Database();
        ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

        var virtualElapsed = elapsed(() -> {
            for (String sql : queries) {
                virtualExecutor.submit(() -> database2.query(sql));
            }
            virtualExecutor.shutdown();
            try {
                virtualExecutor.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        int virtualMinConnections = database2.getMinAvailable();

        System.out.println("\n📊 Virtual Threads:");
        System.out.println("  Tiempo: " + virtualElapsed.timeMillis() + " ms");
        System.out.println("  Mínimo conexiones disponibles: " + virtualMinConnections);
        System.out.println("  Conexiones usadas (máx): " + (10 - virtualMinConnections));

        // Análisis
        double timeDiff = Math.abs(virtualElapsed.timeMillis() - normalElapsed.timeMillis());
        double timePercent = (timeDiff / normalElapsed.timeMillis()) * 100;

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📚 ANÁLISIS:");
        System.out.println("  Diferencia de tiempo: " + String.format("%.1f ms (%.1f%%)", timeDiff, timePercent));

        if (timePercent < 10) {
            System.out.println("  ✅ Los tiempos son SIMILARES (~" + String.format("%.1f%%", timePercent) + " diferencia)");
        } else {
            System.out.println("  ⚠️  Hay diferencia significativa: " + String.format("%.1f%%", timePercent));
        }

        System.out.println("\n💡 LECCIÓN CLAVE:");
        System.out.println("  El cuello de botella NO son los threads, sino las 10 conexiones DB.");
        System.out.println("  Por eso virtual threads NO mejoran significativamente el rendimiento.");
        System.out.println("  Ambos tipos de threads esperan por las mismas 10 conexiones.");

        // Assertions realistas
        // Los tiempos deberían ser similares (dentro del 20% de diferencia)
        similars(virtualElapsed.timeMillis(), normalElapsed.timeMillis());

        // Ambos deberían usar todas las conexiones (mínimo cercano a 0)
        assertTrue(normalMinConnections <= 3,
                "Platform threads debería usar casi todas las conexiones");
        assertTrue(virtualMinConnections <= 3,
                "Virtual threads debería usar casi todas las conexiones");
    }

    public static <T extends Number> void similars(T a, T b) {
        double ratio =  a.doubleValue() / b.doubleValue();
        assertTrue(ratio > 0.8 && ratio < 1.2,
                "Los tiempos deberían ser similares (ratio: " + ratio + ")");
    }

    @Test
    void demonstrateBottleneck() throws InterruptedException {
        System.out.println("\n🔬 DEMOSTRACIÓN: ¿Qué pasa si aumentamos el pool de conexiones?");
        System.out.println("=".repeat(60));

        // Test con diferentes tamaños de pool
        int[] poolSizes = {5, 10, 50, 100};

        for (int poolSize : poolSizes) {
            class ConfigurableDatabase {
                private final Semaphore connections;

                ConfigurableDatabase(int size) {
                    this.connections = new Semaphore(size, false);
                }

                public void query() {
                    try {
                        connections.acquire();
                        Thread.sleep(ThreadLocalRandom.current().nextLong(50, 200));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        connections.release();
                    }
                }
            }

            ConfigurableDatabase db = new ConfigurableDatabase(poolSize);

            long start = System.currentTimeMillis();
            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
            for (int i = 0; i < 1000; i++) {
                executor.submit(db::query);
            }
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.MINUTES);
            long time = System.currentTimeMillis() - start;

            System.out.printf("Pool size %3d: %5d ms (throughput: %.1f queries/sec)%n",
                    poolSize, time, 1000.0 / (time / 1000.0));
        }

        System.out.println("\n💡 Observa cómo aumentar el pool mejora el rendimiento.");
        System.out.println("   El limitante NO son los threads, son las conexiones DB.");
    }
}