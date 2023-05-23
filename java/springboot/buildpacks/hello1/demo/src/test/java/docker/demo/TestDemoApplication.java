package docker.demo;

import docker.demo.config.TestContainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestDemoApplication {

  public static void main(String[] args) {
    SpringApplication.from(DemoApplication::main)
        .with(TestContainersConfiguration.class)
        .run(args);
  }
}
