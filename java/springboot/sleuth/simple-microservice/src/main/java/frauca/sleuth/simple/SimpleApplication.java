package frauca.sleuth.simple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Slf4j
public class SimpleApplication {

    @Autowired
    Tracer tracer;

    public static void main(String[] args) {
        SpringApplication.run(SimpleApplication.class);
    }

    @GetMapping
    @RequestMapping("/hello")
    @NewSpan("hello-controller")
    public ResponseEntity<Greeting> hello() {
        log.info("hello has been called");
        Span newSpan = this.tracer.nextSpan().name("calculateTax");
        try (Tracer.SpanInScope ws = this.tracer.withSpan(newSpan.start())) {

            newSpan.tag("taxValue", "helloWorld");
            log.info("Inside span");
            newSpan.event("taxCalculated");
            log.info("Inside span after event");
        } finally {
            // Once done remember to end the span. This will allow collecting
            // the span to send it to a distributed tracing system e.g. Zipkin
            newSpan.end();
        }
        log.info("everything ahs been done");
        return ResponseEntity.ok(Greeting.say("hello"));
    }
}
