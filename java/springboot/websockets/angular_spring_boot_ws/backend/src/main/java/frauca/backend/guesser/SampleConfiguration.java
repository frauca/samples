package frauca.backend.guesser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SampleConfiguration {

    @Bean
    public List<String> words(){
        return Arrays.asList("one","word");
    }
}
