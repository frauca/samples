package com.example.springbootreactor;

import com.example.springbootreactor.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
@Slf4j
class SpringBootReactorApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void ejemplo3() throws InterruptedException {
        CountDownLatch isFinished = new CountDownLatch(1);
        Flux<Long> intervals = Flux.interval(Duration.ofSeconds(1),Duration.ofMillis(500)).take(4);
        Flux<Integer> numbers = Flux.range(0,4);
        Flux<Integer> pace = numbers.zipWith(intervals,(n,time)->n);

        log.info("Subscribe");
        pace.subscribe(n -> log.info("interval test "+n),
                e->log.error("We have some problem",e),
                ()->{
                    log.info("We are done");
                    isFinished.countDown();
                });
        isFinished.await();
    }

    @Test
    private void ejemplo2() {
        Flux<Integer> first = Flux.range(1, 4);
        Flux<Integer> second = Flux.range(10, 7);
        Flux<String> zipped = first.zipWith(second,(a, b) -> String.format("%d.%d", a, b));
        zipped.subscribe(log::info);
    }

    @Test
    private void ejemplo1() {
        Flux.just(successStrings())
                .doOnNext(name -> {
                    if (name.isBlank()) {
                        throw new RuntimeException("text could not be empty");
                    }
                })
                .map(name -> User.builder().name(name).build())
                .subscribe(user -> log.info(user.toString()),
                        error -> log.error("unexpected"),
                        () -> log.info("we are done"));
    }

    private String[] successStrings() {
        return new String[]{"Anna", "Paco", "Julia"};
    }

    private String[] withError() {
        return new String[]{"Maria", "", "Pablo", "Marc"};
    }

}
