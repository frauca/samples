package com.frauca;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Generics {

    @Test
    void objectVsGeneric(){
        List<Object> objects = new ArrayList<>();
        List<?> generics = new LinkedList<>();
        objects.add("algo");
        //generics.add("algo"); invalid as I can only read
        List<? extends Number> numbers = new ArrayList<>();
        //numbers.add(Integer.valueOf(1)); invalid
        List<Number> numbers1 = new ArrayList<>();
        numbers1.add(Integer.valueOf(1));
        numbers1.add(Long.valueOf(1l));
    }

    @Test
    void sumAll(){
        List<? extends Number> nums = List.of(1,2,3,4,5);
        Double result = nums.stream().mapToDouble(Number::doubleValue).sum();
        assertEquals(15,result);
    }

    @Test
    void addAll(){
        List<? extends Number> nums = List.of(1,2,3,4,5);
        List<? super Number> extendible = new ArrayList<>();
        for(var n:nums){
            extendible.add(n);
        }
        assertEquals(nums,extendible);
    }
}
