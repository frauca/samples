/*
 * Copyright (c) 2020 by Marfeel Solutions (http://www.marfeel.com)
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Marfeel Solutions S.L and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Marfeel Solutions S.L and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Marfeel Solutions SL.
 */

package com.example.springwebflux.product;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Component
public class ProductInitialData {

    private final ProductRespository repository;

    public ProductInitialData(ProductRespository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initialData(){
        Flux.from(repository.deleteAll())
                .doOnComplete(
                        ()->{
                            Flux.just(
                                    Product.create("television",10d),
                                    Product.create("radio",2d)
                            ).flatMap(repository::save)
                                    .subscribe();
                        }
                ).subscribe();
    }
}
