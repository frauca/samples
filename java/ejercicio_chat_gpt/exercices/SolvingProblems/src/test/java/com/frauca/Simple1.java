package com.frauca;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

record Persona(String name,int age){}

public class Simple1 {
    @Test
    void orderPerson(){
        var personas = List.of(
                new Persona("Paco", 24),
                new Persona("Albaro", 1),
                new Persona("Paco",1)
        );
        var sorted = personas.stream().sorted(Comparator.comparingInt(Persona::age).thenComparing(Persona::name)).toList();
        var expected = List.of(
                new Persona("Albaro", 1),
                new Persona("Paco",1),
                new Persona("Paco", 24)
        );
        assertEquals(sorted,expected);
    }

    @Test
    void operateStreams(){
        List<String> frases = List.of(
                "Hola Mundo",
                "Aprender Java",
                "Hola Streams",
                "Java Streams y Lambdas"
        );
        var palabras = frases.stream()
                .flatMap(frase-> Stream.of(frase.split(" ")))
                .map(palabra -> palabra.toLowerCase())
                .sorted()
                .reduce(new ArrayList<>(),(lista,palabra)->{
                    if (! lista.contains(palabra)) {
                        lista.add(palabra);
                    }
                    return lista;
                },(l1,l2)->{
                    var result = new ArrayList<>(l1);
                    result.addAll(l2);
                    return result;
                });
        assertEquals(palabras,List.of(
                "aprender",
                "hola",
                "java",
                "lambdas",
                "mundo",
                "streams",
                "y"));
    }

    @Test
    void mltiThread() throws InterruptedException {
        final var counter = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(1000);
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                counter.incrementAndGet();
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(counter.get(),1000);
    }
}


