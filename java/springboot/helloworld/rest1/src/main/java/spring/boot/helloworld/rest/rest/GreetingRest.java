package spring.boot.helloworld.rest.rest;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.boot.helloworld.rest.model.Greeting;

@RestController
public class GreetingRest {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting() {
    	logger.info("greeting");
    	String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
