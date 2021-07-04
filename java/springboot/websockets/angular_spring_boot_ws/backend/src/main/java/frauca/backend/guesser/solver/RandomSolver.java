package frauca.backend.guesser.solver;

import frauca.backend.guesser.Answer;
import frauca.backend.guesser.Riddle;

import java.util.Random;

public class RandomSolver implements Solver{
    private final Random random;

    RandomSolver(){
        this(new Random());
    }

    RandomSolver(Random random) {
        this.random = random;
    }

    @Override
    public Answer solve(Riddle riddle) {
        return Answer.builder()
                .guess(guess(riddle))
                .build();
    }


    int guess(Riddle riddle){
        int bound = riddle.getMax() - riddle.getMin();
        if(bound<=0){
            return riddle.getMin();
        }
        return random.nextInt(bound)+riddle.getMin();
    }
}
