package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    private static final char HOLE = '0';
    private static final char SAVE = '.';
    private static final String SPEED = "SPEED";
    private static final String SLOW = "SLOW";
    private static final String JUMP = "JUMP";
    private static final String ERROR = "ERROR";


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        //String line = "..............00000......0000.....00......";
        String line ="...........0..................";

        List<String> all = getConvination(0, 0 , line);

        System.out.println(all);
    }

    public static List<String> getConvination(int pos, int S, String L){
        return allConvinations(pos,S,L,Collections.emptyList()).stream()
                .findFirst()
                .orElse(null);
    }

    public static List<List<String>> allConvinations(int pos, int S, String L, List<String> actions) {
        int max = L.length();
        List<List<String>> allConvinations = new ArrayList<>();
        if (pos + S - 1 >= max) {
            return Collections.singletonList(addAction(actions, SPEED));
        } else if (containsHole(pos, L, S)) {
            if (saveIfApply(pos, S, L, JUMP)) {
                return allConvinations(
                        pos + S,
                        S,
                        L,
                        addAction(actions, JUMP));
            }
        } else {
            if (saveIfApply(pos, S, L, SPEED)) {
                allConvinations.addAll(
                        allConvinations(
                                pos + S + 1,
                                S + 1,
                                L,
                                addAction(actions, SPEED)));
            }
            if (saveIfApply(pos, S, L, SLOW)) {
                allConvinations.addAll(
                        allConvinations(
                                pos + S - 1,
                                S - 1,
                                L,
                                addAction(actions, SLOW)));
            }

        }
        if (allConvinations.isEmpty()) {
            allConvinations.add(
                    addAction(actions, ERROR)
            );
        }
        return allConvinations;
    }

    public static List<String> addAction(List<String> actions, String action) {
        List<String> cloned = new ArrayList();
        cloned.addAll(actions);
        cloned.add(action);
        return cloned;
    }

    public static boolean saveIfApply(int pos, int S, String line, String action) {
        if (SLOW.equals(action)) {
            return S>1 &&
                    SAVE == line.charAt(pos + S - 1);
        } else if (JUMP.equals(action)) {
            return pos + S < line.length() &&
                    SAVE == line.charAt(pos + S);
        } else {
            return S<=50 &&
                    pos + S + 1 < line.length() &&
                    SAVE == line.charAt(pos + S + 1);
        }
    }

    public static boolean containsHole(int pos, String L, int S) {
        String nextStep = L.substring(pos, Math.min(pos + S, L.length() - 1));
        return nextStep.indexOf(HOLE) >= 0;
    }

}
