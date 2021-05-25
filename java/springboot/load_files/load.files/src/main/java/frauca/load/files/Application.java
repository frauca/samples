package frauca.load.files;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;

import java.io.*;

@SpringBootApplication
@Slf4j
public class Application implements CommandLineRunner {

    @Value("${file}")
    @NonNull
    private Resource file_to_read;

    @Value("${intention_file}")
    @NonNull
    private Resource file_intention;

    public Application() {
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("We are going to print {}", file_to_read.toString());
        log.info(read(file_to_read.getInputStream()));
        log.info(read(file_intention.getInputStream()));
    }

    String read(InputStream input) throws IOException {
        Reader reader = new InputStreamReader(input);
        return new BufferedReader(reader).readLine();
    }
}
