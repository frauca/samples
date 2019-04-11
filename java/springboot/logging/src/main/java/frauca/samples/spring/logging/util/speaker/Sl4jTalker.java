package frauca.samples.spring.logging.util.speaker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Talks throught sl4j
 */
@Slf4j
@Component
public class Sl4jTalker implements Talker {


  @Override
  public void talk() {
    log.info("Slf4j info speaking");
    log.debug("Slf4j debug speaking");
    log.trace("Slf4j trace speaking");
  }
}
