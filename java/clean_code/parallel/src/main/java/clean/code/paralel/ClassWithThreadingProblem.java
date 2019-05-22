package clean.code.paralel;

public class ClassWithThreadingProblem {

  int nextId;

  public int takeNextId() {
    return nextId++;
  }

}


