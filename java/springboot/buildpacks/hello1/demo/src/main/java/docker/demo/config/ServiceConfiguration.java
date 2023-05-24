package docker.demo.config;

import docker.demo.event.PersonRepository;
import docker.demo.event.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

@Configuration
public class ServiceConfiguration {

  @Bean
  PersonService personService(PersonRepository repo){
    return new PersonService(repo);
  }
}
