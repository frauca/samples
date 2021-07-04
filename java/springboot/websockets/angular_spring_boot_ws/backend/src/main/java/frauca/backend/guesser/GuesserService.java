package frauca.backend.guesser;

import frauca.backend.guesser.solver.Solver;

public class GuesserService {

    private final Solver solver;

    public GuesserService(Solver solver) {
        this.solver = solver;
    }

    public Answer guest(Riddle riddle){
        return solver.solve(riddle);
    }
}
