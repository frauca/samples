package docker.demo.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
public class TestContainersConfiguration {

  @Bean
  @ServiceConnection
  PostgreSQLContainer postgreSQLContainer(){
    return new PostgreSQLContainer("postgres:15.3");
  }
}
