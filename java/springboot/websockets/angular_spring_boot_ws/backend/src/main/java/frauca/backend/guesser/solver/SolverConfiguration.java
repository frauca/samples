package frauca.backend.guesser.solver;

import frauca.backend.guesser.GuesserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolverConfiguration {
    @Bean
    public Solver solver() {
        return new RandomSolver();
    }

}
