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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@Builder
@JsonDeserialize(builder = Product.ProductBuilder.class)
public class Product {

    @Id
    private String id;
    private String name;
    private Double price;
    @Builder.Default
    private Date createdAt = new Date();

    public static Product create(String name, Double price) {
        return builder()
                .name(name)
                .price(price)
                .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ProductBuilder {

    }
}
