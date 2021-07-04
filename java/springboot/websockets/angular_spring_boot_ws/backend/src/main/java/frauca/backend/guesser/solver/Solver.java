package frauca.backend.guesser.solver;

import frauca.backend.guesser.Answer;
import frauca.backend.guesser.Riddle;

public interface Solver {
    public Answer solve(Riddle riddle);
}
