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

package com.example.demo;

import org.springframework.boot.SpringApplication;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SecondExample {

    private static Random random = new Random();

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        List<List<String>> l = fullList();

        List<List<String>> sorted = l.stream()
                .filter(list->list.stream().anyMatch(c->c.indexOf('n')>-1))
                .sorted(Comparator.comparing(list->list.size()))
                .collect(Collectors.toList());

        List<String> result = l.stream()
                .filter(list->list.stream().anyMatch(c->c.indexOf('n')>-1))
                .sorted(Comparator.<List,Integer>comparing(List::size)
                        .reversed())
        .findFirst()
                .orElse(null);
        System.out.println(l);
        System.out.println("-----------");
        System.out.println(sorted);
        System.out.println("-----------");
        System.out.println(result);
    }

    private static List<List<String>> fullList(){
        List<List<String>> result = new ArrayList<>();
        int numofElements = random.nextInt(10)+1;
        for(int i = 0;i<numofElements;i++){
            result.add(randomList());
        }
        return result;
    }

    private static List<String> randomList(){
        List<String> result = new ArrayList<>();
        int numofElements = random.nextInt(10);
        for(int i = 0;i<numofElements;i++){
            result.add(randomString());
        }
        return result;
    }

    private static String randomString(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
