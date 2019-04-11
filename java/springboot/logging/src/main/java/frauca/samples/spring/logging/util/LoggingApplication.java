package frauca.samples.spring.logging.util;

import frauca.samples.spring.logging.util.speaker.Talker;
import java.util.List;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Test the logging
 */
@SpringBootApplication
@Log
public class LoggingApplication implements CommandLineRunner {



  List<Talker> talkers;

  public LoggingApplication(List<Talker> talkers){
    this.talkers=talkers;
  }

  public static void main(String[] args) {
    SpringApplication.run(LoggingApplication.class, args);
  }


  @Override
  public void run(String... args) throws Exception {
    log.info("Start applicatoin");
    log.fine("App fine message");
    log.finer("App finer message");
    log.finest("App finest message");
    talkers.stream().forEach(t->t.talk());
    log.info("End program");

  }
}
