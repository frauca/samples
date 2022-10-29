package frauca.metrics.person;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController()
@RequestMapping("/persons")
public class PersonController {

    private List<Person> db = List.of(new Person("Jaume"), new Person("Manel"));

    @GetMapping
    public Flux<Person> list(){
        return Flux.fromIterable(db);
    }
}
