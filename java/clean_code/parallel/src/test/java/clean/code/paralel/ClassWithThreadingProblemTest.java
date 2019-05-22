package clean.code.paralel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.Test;

public class ClassWithThreadingProblemTest {

  @Test
  public void whenRun100Times_ThenAlwaysIncrent2() throws ExecutionException, InterruptedException {
    final ClassWithThreadingProblem counter = new ClassWithThreadingProblem();
    for(int i=0;i<1000;i++){
      int current = counter.nextId;
      Callable<Integer> increment = new Callable<Integer>() {
        public Integer call() throws Exception {
          return counter.takeNextId();
        }
      };
      Future<Integer> c1 = Executors.newSingleThreadExecutor().submit(increment);
      Future<Integer> c2 = Executors.newSingleThreadExecutor().submit(increment);
      Future<Integer> c3 = Executors.newSingleThreadExecutor().submit(increment);
      c1.get();
      c2.get();
      c3.get();
      if(current+3 != counter.nextId)
        return;
    }
    fail("It must fail. The class is not thread save");
  }
}
