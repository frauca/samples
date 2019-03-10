package spring.boot.helloworld.rest.integration.rest.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.boot.helloworld.rest.bussiness.CutomerService;
import spring.boot.helloworld.rest.model.Customer;

@Configuration
public class LoadDataBase {

	@Bean
	CommandLineRunner loadDatabase(CutomerService cs) {
		return args ->{
			cs.save(new Customer("roger", "password"));
		};
	}
}
