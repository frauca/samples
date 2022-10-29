package frauca.metrics.person;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/persons")
public class PersonController {

    private final List<Person> db;
    private final Counter listedTimes;
    private final Timer elapsed;

    public PersonController(MeterRegistry registry){
        listedTimes = registry.counter("persons_requests");
        db = registry.gauge("persons", new ArrayList<Person>(), ArrayList::size);
        elapsed = registry.timer("persons_elapsed");
    }

    @GetMapping
    public Flux<Person> list(){
        return elapsed.record(()->{
            listedTimes.increment();
            gauge();
            return Flux.fromIterable(db);
        });
    }

    private void gauge(){
        final var count = listedTimes.count();
        if(count %2==0){
            db.add(new Person("Jaume"));
        } else if (count % 3 == 0){
            db.add(new Person("Manel"));
        } else if (count % 5 == 0){
            db.add(new Person("Pep"));
        }else if (count % 7 == 0){
            db.add(new Person("Teo"));
        } else if (count % 11 == 0){
            db.add(new Person("Zoe"));
        } else {
            db.clear();
        }
    }
}
