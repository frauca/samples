package com.example.springwebflux.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class ProductServiceTest {


    @Autowired
    ProductInitialData service;

    @Autowired
    ProductRespository respository;

    @AfterEach
    public void cleanUp(){
        respository.deleteAll().subscribe();
    }

    @Test
    public void addProducts() throws InterruptedException {
        service.initialData();
        TimeUnit.SECONDS.sleep(1);
        Product television = Product.builder().name("television").build();
        Flux<Product> televisionSearch = respository.findByName(television.getName());

        StepVerifier.create(televisionSearch)
                .assertNext(product -> product.getName().equals(television.getName()))
                .expectComplete()
        .verify();

        televisionSearch = respository.findAll(Example.of(television));

        StepVerifier.create(televisionSearch)
                .assertNext(product -> product.getName().equals(television.getName()))
                .verifyComplete();

    }
}
