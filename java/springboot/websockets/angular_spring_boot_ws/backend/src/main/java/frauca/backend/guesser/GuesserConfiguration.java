package frauca.backend.guesser;

import frauca.backend.guesser.solver.Solver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuesserConfiguration {
    @Bean
    public GuesserService guesser(Solver solver){
        return new GuesserService(solver);
    }
}
