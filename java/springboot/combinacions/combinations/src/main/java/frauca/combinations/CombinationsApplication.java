package frauca.combinations;

import frauca.combinations.sums.CombinationsMainService;
import frauca.combinations.sums.CombinationsService;
import frauca.combinations.sums.IntegerDeserializer;
import frauca.combinations.sums.OperationsSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CombinationsApplication implements CommandLineRunner {

	private final CombinationsMainService service;

	public CombinationsApplication() {
		IntegerDeserializer deserialize = new IntegerDeserializer();
		CombinationsService combinations = new CombinationsService();
		OperationsSerializer serializer = new OperationsSerializer();
		service = new CombinationsMainService(deserialize ,combinations,serializer);
	}

	public static void main(String[] args) {
		SpringApplication.run(CombinationsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(args!=null&&args.length>0) {
			log.info(
					service.resolve(args[0])
			);
		}
	}
}
