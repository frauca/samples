package frauca.backend.guesser;

import frauca.backend.guesser.solver.Solver;
import frauca.backend.sampler.RiddleSampler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuesserServiceTest {

    @Mock
    Solver solver;

    @InjectMocks
    GuesserService service;

    @Test
    void guesser_calls_solver(){
        Riddle riddle = RiddleSampler.sample();
        when(solver.solve(eq(riddle))).thenReturn(
            Answer.builder().guess(1).build()
        );

        Answer answer = service.guest(riddle);
        verify(solver,times(1))
                .solve(eq(riddle));
        assertThat(answer.getGuess()).isEqualTo(1);
    }
}