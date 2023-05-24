package docker.demo.event;



import static org.assertj.core.api.Assertions.assertThat;

import docker.demo.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonRepositoryTest extends BaseTest {

  @Autowired PersonRepository repo;

  @Test
  void crudTest(){
    final var person = PersonSampler.builder().id(null).build();

    final var saved = repo.save(person);

    final var mayBeFound = repo.findById(saved.id());

    assertThat(mayBeFound).isPresent().get().isEqualTo(saved);
  }
}
