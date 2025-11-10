package com.frauca;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Elapsed {

    record Metrics(
            long timeMillis,      // Tiempo de reloj (wall-clock time)
            long memoryBytes,     // Memoria usada
            long cpuTimeMillis,   // Tiempo de CPU usado
            long userTimeMillis,  // Tiempo de CPU en modo usuario
            long systemTimeMillis // Tiempo de CPU en modo sistema (kernel)
    ) {
        @Override
        public String toString() {
            return String.format(
                    "Tiempo: %d ms, CPU: %d ms (%.1f%%), Memoria: %.2f MB, User: %d ms, System: %d ms",
                    timeMillis,
                    cpuTimeMillis,
                    (cpuTimeMillis * 100.0 / timeMillis),
                    memoryBytes / 1024.0 / 1024.0,
                    userTimeMillis,
                    systemTimeMillis
            );
        }

        // Ratio de uso de CPU (útil para detectar I/O vs CPU bound)
        public double cpuUtilization() {
            return (cpuTimeMillis * 100.0 / timeMillis);
        }
    }

    static Runtime runtime = Runtime.getRuntime();

    public static Metrics run(Runnable task) {
        // Preparar medición
        System.gc();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // ThreadMXBean para medir CPU
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        // Memoria inicial
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        // Tiempo inicial (wall-clock)
        long startTime = System.currentTimeMillis();

        // Tiempo de CPU inicial
        long startCpuTime = threadMXBean.getCurrentThreadCpuTime();
        long startUserTime = threadMXBean.getCurrentThreadUserTime();

        // Ejecutar tarea
        task.run();

        // Tiempos finales
        long endTime = System.currentTimeMillis();
        long endCpuTime = threadMXBean.getCurrentThreadCpuTime();
        long endUserTime = threadMXBean.getCurrentThreadUserTime();

        // Memoria final
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();

        // Calcular diferencias
        long wallClockTime = endTime - startTime;
        long cpuTime = (endCpuTime - startCpuTime) / 1_000_000; // Convertir a ms
        long userTime = (endUserTime - startUserTime) / 1_000_000;
        long systemTime = cpuTime - userTime;
        long memoryUsed = finalMemory - initialMemory;

        return new Metrics(wallClockTime, memoryUsed, cpuTime, userTime, systemTime);
    }
}
