package docker.demo.event;

public class PersonService {

  private final PersonRepository repository;

  public PersonService(PersonRepository repository) {
    this.repository = repository;
  }

  public Person create(Person person){
    if(person.id()!=null) {
      return repository.findById(person.id()).orElseThrow();
    }
    return repository.save(person);
  }

  public Iterable<Person> all(){
    return repository.findAll();
  }
}
