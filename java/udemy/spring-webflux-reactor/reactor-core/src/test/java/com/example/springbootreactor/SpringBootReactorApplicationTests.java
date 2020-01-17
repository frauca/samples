package com.example.springbootreactor;

import com.example.springbootreactor.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class SpringBootReactorApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testingMe(){
        StepVerifier.create(Flux.just("boo","car"))
                .expectTimeout(Duration.ofMillis(1))
                .verify();
        StepVerifier.withVirtualTime(() -> Flux.just("boo","car"))
                .verifyComplete();
    }

    @Test
    public void capitalize(){
        Mono<String> capi = Mono.just("roger")
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1));

    }

    @Test
    public void backPressure(){
        Flux.range(1,10)
                .log()
                .limitRate(2)
                .log()
                .subscribe(this::print);
    }

    @Test
    public void createFlux(){
        Flux.create(emitter ->{
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                int  counter = 0;
                @Override
                public void run() {
                    emitter.next(counter++);
                    if(counter>5){
                        timer.cancel();
                        emitter.complete();
                    }
                }
            }, 500, 500);
        })
                .take(5)
                .doOnNext(this::print)
                .blockLast();
    }

    @Test
    void ejemplo3() throws InterruptedException {
        Flux<Long> intervals = Flux.interval(Duration.ofSeconds(1),Duration.ofMillis(500)).take(4);
        Flux<Integer> numbers = Flux.range(0,4);
        Flux<Integer> pace = numbers.zipWith(intervals,(n,time)->n);

        log.info("Subscribe");
        pace
                .doOnNext(this::print)
                .blockLast();
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

    private void print(Object whatever){
        log.info(whatever.toString());
    }

}
