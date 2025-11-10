package spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Sample1 {
    @Service
    public static class HelloService{
        public String obtenerSaludo(){
            return "Hello";
        }
    }

    @RestController
    public static class HelloController{
        @Autowired
        HelloService service;

        @GetMapping("/hi")
        public String obtenerSaludo(){
            return service.obtenerSaludo();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Sample1.class, args);
    }
}

