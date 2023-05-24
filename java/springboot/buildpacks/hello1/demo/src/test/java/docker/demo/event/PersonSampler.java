package docker.demo.event;

import java.util.UUID;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

public class PersonSampler {

  public static Person sample(){
    return new PersonSampler().build();
  }

  public static PersonSampler builder(){
    return new PersonSampler();
  }

  UUID id = UUID.randomUUID();

  public PersonSampler id(UUID id) {
    this.id = id;
    return this;
  }

  String mail = RandomStringUtils.randomAlphabetic(4)+"@mail.com";
  String name = RandomStringUtils.randomAlphabetic(5)+" "+RandomStringUtils.randomAlphabetic(8);

  public Person build(){
    return new Person(id,mail, name);
  }
}
