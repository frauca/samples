package docker.demo.event;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import docker.demo.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

class PersonControllerTest extends BaseTest {

  @Autowired
  TestRestTemplate client;


  @Test
  void crudOperation()  {
    final var person = PersonSampler.unsaved();
    final var savedResponse = client.postForEntity("/persons",person,Person.class);
    assertThat(savedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    final var saved = savedResponse.getBody();

    assertThat(saved.name()).isEqualTo(person.name());
    assertThat(saved.mail()).isEqualTo(person.mail());
    assertThat(saved.id()).isNotNull();

    final var allPersonsResponse = client.getForEntity("/persons",Person[].class);

    assertThat(allPersonsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(allPersonsResponse.getBody()).contains(saved);
  }
}
