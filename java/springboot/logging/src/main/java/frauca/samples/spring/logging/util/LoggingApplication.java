package frauca.samples.spring.logging.util;

import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Test the logging
 */
@SpringBootApplication
@Log
public class LoggingApplication implements CommandLineRunner {


  public static void main(String[] args) {
    SpringApplication.run(LoggingApplication.class, args);
  }


  @Override
  public void run(String... args) throws Exception {
    log.info("Start applicatoin");
    log.fine("fine message");
    log.finer("finer message");
    log.finest("message");
    log.info("End program");
  }
}
