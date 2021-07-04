package frauca.backend.guesser.solver;

import frauca.backend.guesser.Answer;
import frauca.backend.guesser.Riddle;
import frauca.backend.sampler.RiddleSampler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RandomSolverTest {

    RandomSolver solver = new RandomSolver(new Random());

    @Test
    void simpleSolve(){
        Riddle riddle = RiddleSampler.sample();
        for(int i =0; i< 10; i++){
            Answer answer = solver.solve(riddle);
            assertThat(answer.getGuess()).isBetween(riddle.getMin(),riddle.getMax());
        }
    }

    @Test
    void impossibleToFail(){
        Riddle sureSuccess = Riddle.builder()
                .min(1)
                .max(1)
                .build();
        Answer answer = solver.solve(sureSuccess);
        assertThat(answer.getGuess()).isEqualTo(1);
    }
}