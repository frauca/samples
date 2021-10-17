package frauca.seulth.zipkin.rest;

import frauca.sleuth.simple.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/hello")
@Slf4j
public class HelloCallerController {

    private final WebClient simpleHello;

    public HelloCallerController(WebClient simpleHello) {
        this.simpleHello = simpleHello;
    }

    @GetMapping
    @NewSpan("say-hello")
    public ResponseEntity<Mono<Greeting>> hello(){
        log.info("we will call hello");
        var greeting = simpleHello.get()
                .uri("/hello")
                .exchangeToMono(response->response.bodyToMono(Greeting.class));
        return ResponseEntity.ok(greeting);
    }
}
