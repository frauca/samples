package org.example;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import jdk.incubator.concurrent.StructuredTaskScope;
import org.junit.jupiter.api.Test;

class StructuredConcurrencyTest {

  @Test
  void sample() throws InterruptedException, ExecutionException {
    try(var scope = new StructuredTaskScope.ShutdownOnFailure()){
      Future<Integer> one = scope.fork(()->1);
      Future<Integer> two = scope.fork(()->2);

      scope.join();
      scope.throwIfFailed();


      assertThat(one.resultNow() + two.resultNow()).isEqualTo(3);
    }
  }

  @Test
  void failing()  throws InterruptedException {
    try(var scope = new StructuredTaskScope.ShutdownOnFailure()){
      Future<Integer> one = scope.fork(()->{
        if (System.currentTimeMillis()>0){
          throw new NullPointerException("error");
        }
        return 1;
      });

      scope.join();
      assertThatThrownBy(()->scope.throwIfFailed())
          .isInstanceOf(ExecutionException.class)
          .cause()
          .isInstanceOf(NullPointerException.class)
          .hasMessage("error");
    }
  }

}
