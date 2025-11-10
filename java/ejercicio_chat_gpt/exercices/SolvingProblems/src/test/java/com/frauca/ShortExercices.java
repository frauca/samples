package com.frauca;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ShortExercices {

    int firstNoneRepeated(int[] nums){

        for(int i=0;i<nums.length;i++){
            int curr = nums[i];
            boolean repeated = false;
            for(int j=0;j<nums.length;j++){
                if(i!=j) {
                    if (curr == nums[j]) {
                        repeated = true;
                        break;
                    }
                }
            }
            if(!repeated){
                return curr;
            }
        }
        return -1;
    }

    int firstNoneRepeatedV2(int[] nums){
        record NumAndPos(int value,int pos){

        }
        Map<Integer,NumAndPos> noneRepeated = new HashMap<>();
        Map<Integer,Boolean> repeated = new HashMap<>();
        for(int i=0;i<nums.length;i++){
            Integer curr = Integer.valueOf(nums[i]);
            if (!repeated.containsKey(curr)) {
                if(noneRepeated.containsKey(curr)){
                    noneRepeated.remove(curr);
                    repeated.put(curr,Boolean.TRUE);
                }else{
                    noneRepeated.put(curr,new NumAndPos(curr,i));
                }
            }
        }
        return noneRepeated.values().stream().sorted(Comparator.comparingInt(NumAndPos::pos)).findFirst().map(NumAndPos::value).orElse(-1);
    }

    @Test
    void firstNotRepeated(){
        assertEquals(5,firstNoneRepeated(new int[]{4, 5, 1, 2, 1, 4, 2}));
        assertEquals(-1,firstNoneRepeated(new int[]{7, 7, 7}));
        assertEquals(5,firstNoneRepeated(new int[]{4, 5, 1, 2,6, 1, 4, 2}));
        assertEquals(2,firstNoneRepeated(new int[]{1,1, 2}));
    }

    class MySloqQueue<T>{
        Stack<T> a = new Stack<>();
        Stack<T> b = new Stack<>();

        void enqueue(T x){
            a.push(x);
            reverseMe();
        }
        T dequeue(){
            return a.pop();
        }
        T peek(){
            T x = a.pop();
            a.push(x);
            return x;
        }
        boolean isEmpty(){
            return a.isEmpty();
        }

        void reverseMe(){
            while (!a.isEmpty()) {
                b.push(a.pop());
            }
            var tmp = b;
            b = a;
            a = tmp;
        }
    }

    class MyFastQueue<T>{
        Stack<T> disorded = new Stack<>();
        Stack<T> ordered = new Stack<>();

        void enqueue(T x){
            disorded.push(x);
        }
        T dequeue(){
            if(!ordered.isEmpty()){
                return ordered.pop();
            }
            bulkToOrdered();
            return ordered.pop();
        }
        T peek(){
            if(ordered.isEmpty()){
                bulkToOrdered();
            }
            if(!ordered.isEmpty()) {
                T x = ordered.pop();
                ordered.push(x);
                return x;
            }
            return null;
        }
        boolean isEmpty(){
            return ordered.isEmpty() && disorded.isEmpty();
        }

        void bulkToOrdered(){
            while (!disorded.isEmpty()) {
                ordered.push(disorded.pop());
            }
        }
    }

    class MyAlreadyImplemented<T>{
        Deque<T> queue = new ArrayDeque<>();

        void enqueue(T x){
            queue.offer(x);
        }
        T dequeue(){
            return queue.poll();
        }
        T peek(){
            return queue.peek();
        }
        boolean isEmpty(){
            return queue.isEmpty();
        }


    }

    @Test
    void testQueue(){
        var q = new MyAlreadyImplemented<>();
        q.enqueue("hola");
        assertEquals("hola",q.peek());
        assertFalse(q.isEmpty());
        assertEquals("hola",q.dequeue());
        assertTrue(q.isEmpty());

        q.enqueue("first");

        q.enqueue("second");
        assertEquals("first",q.dequeue());
        assertEquals("second",q.peek());
        assertFalse(q.isEmpty());
        assertEquals("second",q.dequeue());
        assertTrue(q.isEmpty());

        q.enqueue("first");
        q.enqueue("second");
        assertEquals("first",q.dequeue());
        q.enqueue("third");
        assertEquals("second",q.dequeue());
        assertFalse(q.isEmpty());
        assertEquals("third",q.peek());
        assertFalse(q.isEmpty());
        assertEquals("third",q.dequeue());
        assertTrue(q.isEmpty());

    }
}
