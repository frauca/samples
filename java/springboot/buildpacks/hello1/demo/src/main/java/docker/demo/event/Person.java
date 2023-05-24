package docker.demo.event;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public record Person(
    @Id UUID id,
    String mail,
    String name
    ) {

}
