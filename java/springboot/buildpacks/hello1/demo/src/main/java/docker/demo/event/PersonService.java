package docker.demo.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonService {

  private final Logger log = LoggerFactory.getLogger(PersonService.class);
  private final PersonRepository repository;

  public PersonService(PersonRepository repository) {
    this.repository = repository;
  }

  public Person create(Person person){
    if(person.id()!=null) {
      log.info("Trying to make a person with id {}. We are getting for it", person.id());
      return repository.findById(person.id()).orElseThrow();
    }
    log.info("Creating person with email {}",person.mail());
    return repository.save(person);
  }

  public Iterable<Person> all(){
    log.info("Get all persons");
    return repository.findAll();
  }
}
