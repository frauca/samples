package com.frauca;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Sorting {
    static Stream<Arguments> sortingData() {
        return Stream.of(
                Arguments.of(new int[] {5, 2, 9, 1, 5}, new int[] {1, 2, 5, 5, 9}),
                Arguments.of(new int[] {3, 8, 6, 2}, new int[] {2, 3, 6, 8}),
                Arguments.of(new int[] {12, 4, 7, 3, 9, 2, 11, 8}, new int[] {2, 3, 4, 7, 8, 9, 11, 12}),
                Arguments.of(new int[] {34, 12, 9, 23, 45, 1, 67, 33, 22, 56, 78, 90, 11, 4},
                        new int[] {1, 4, 9, 11, 12, 22, 23, 33, 34, 45, 56, 67, 78, 90}),
                Arguments.of(new int[] {}, new int[] {}),
                Arguments.of(new int[] {42}, new int[] {42}),
                Arguments.of(new int[] {5, 5, 5, 5}, new int[] {5, 5, 5, 5})
        );
    }

    void swapInplace(int[] arr,int pos, int newPos){
        int previous = arr[pos];
        arr[pos] = arr[newPos];
        arr[newPos] = previous;
    }

    int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) result[k++] = left[i++];
            else result[k++] = right[j++];
        }

        while (i < left.length) result[k++] = left[i++];
        while (j < right.length) result[k++] = right[j++];

        return result;
    }

    int[] insertionSort(int[] unsorted){
        for (int i = 1; i < unsorted.length; i++) {
            int key = unsorted[i];
            int j = i - 1;
            while (j >= 0 && unsorted[j] > key) {
                unsorted[j + 1] = unsorted[j];
                j--;
            }
            unsorted[j + 1] = key;
        }
        return unsorted;
    }

    int[] selectionSort(int[] unsorted){
        for(int i=0;i<unsorted.length;i++){
            for(int j=i+1;j<unsorted.length;j++){
                if (unsorted[i]>unsorted[j]){
                    swapInplace(unsorted,i,j);
                }
            }
        }
        return unsorted;
    }

    int[] bubleSort(int[] unsorted){
        for(int i=0;i<unsorted.length;i++){
            boolean hasSwaped = false;
            for(int j=0;j<unsorted.length-1 ;j++){
                if (unsorted[j]>unsorted[j+1]){
                    swapInplace(unsorted,j,j+1);
                    hasSwaped=true;
                }
            }
            if(!hasSwaped){
                break;
            }
        }
        return unsorted;
    }


    int[] mergeSort(int[] arr) {
        if (arr.length <= 1) return arr;

        int mid = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, mid);
        int[] right = Arrays.copyOfRange(arr, mid, arr.length);

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }


    int[] quickSort(int[] arr) {
        if (arr.length <= 1) return arr;
        int pivotPoss = arr.length-1;
        for(int i=0;i<arr.length-1;i++){
            if(arr[i]>arr[pivotPoss]){
                swapInplace(arr,i,pivotPoss);
            }
        }
        int[] left = Arrays.copyOfRange(arr, 0, pivotPoss);
        int[] right = Arrays.copyOfRange(arr, pivotPoss, arr.length);

        left = quickSort(left);
        right = quickSort(right);

        return IntStream.concat(Arrays.stream(left),Arrays.stream(right)).toArray();
    }

    @ParameterizedTest
    @MethodSource("sortingData")
    void testselectionSort(int[] input, int[] expected) {
        int[] result = Arrays.copyOf(input, input.length);
        selectionSort(result);
        assertArrayEquals(expected, result, "El array no se ordenó correctamente");
    }

    @ParameterizedTest
    @MethodSource("sortingData")
    void testbubleSort(int[] input, int[] expected) {
        int[] result = Arrays.copyOf(input, input.length);
        bubleSort(result);
        assertArrayEquals(expected, result, "El array no se ordenó correctamente");
    }

    @ParameterizedTest
    @MethodSource("sortingData")
    void testInsertionSort(int[] input, int[] expected) {
        int[] result = Arrays.copyOf(input, input.length);
        insertionSort(result);
        assertArrayEquals(expected, result, "El array no se ordenó correctamente");
    }

    @ParameterizedTest
    @MethodSource("sortingData")
    void testMergeSort(int[] input, int[] expected) {
        int[] result = Arrays.copyOf(input, input.length);
        result = mergeSort(result);
        assertArrayEquals(expected, result, "El array no se ordenó correctamente");
    }

    @ParameterizedTest
    @MethodSource("sortingData")
    void testQuicSort(int[] input, int[] expected) {
        int[] result = Arrays.copyOf(input, input.length);
        result = quickSort(result);
        assertArrayEquals(expected, result, "El array no se ordenó correctamente");
    }


        static Stream<TestCase> provideArrays() {
            return Stream.of(
                    new TestCase(new int[]{1, 2, 3, 4, 5}, 3, 3),
                    new TestCase(new int[]{1, 2, 3, 4, 5}, 1, 1),
                    new TestCase(new int[]{1, 2, 3, 4, 5}, 5, 5),
                    new TestCase(new int[]{1, 2, 3, 4, 5}, 6, -1)
            );
        }

        @ParameterizedTest
        @MethodSource("provideArrays")
        void testLinear(TestCase tc) {
            int result = findIterative(tc.values, tc.element);
            assertEquals(tc.expected, result);
        }

        @ParameterizedTest
        @MethodSource("provideArrays")
        void testBinaryIterative(TestCase tc) {
            int result = findBinary(tc.values, tc.element);
            Assertions.assertEquals(tc.expected, result);
        }

        @ParameterizedTest
        @MethodSource("provideArrays")
        void testBinaryRecursive(TestCase tc) {
            int result = findBinaryRecursive(tc.values, tc.element, 0, tc.values.length - 1);
            Assertions.assertEquals(tc.expected, result);
        }

        int findIterative(int[] values, int element) {
            for (int i : values) {
                if (i == element) {
                    return element;
                }
            }
            return -1;
        }

        int findBinary(int[] values, int element) {
            int lower = 0;
            int upper = values.length - 1;
            while (lower <= upper) {
                int pos = lower + (upper - lower) / 2;
                if (values[pos] == element) {
                    return element;
                }
                if (values[pos] > element) {
                    upper = pos - 1;
                } else {
                    lower = pos + 1;
                }
            }
            return -1;
        }

        int findBinaryRecursive(int[] values, int element, int lower, int upper) {
            if (lower > upper) return -1;

            int pos = lower + (upper - lower) / 2;
            if (values[pos] == element) {
                return element;
            }
            if (values[pos] > element) {
                return findBinaryRecursive(values, element, lower, pos - 1);
            } else {
                return findBinaryRecursive(values, element, pos + 1, upper);
            }
        }

        // Clase para agrupar casos de test
        static class TestCase {
            int[] values;
            int element;
            int expected;

            TestCase(int[] values, int element, int expected) {
                this.values = values;
                this.element = element;
                this.expected = expected;
            }
        }
}
