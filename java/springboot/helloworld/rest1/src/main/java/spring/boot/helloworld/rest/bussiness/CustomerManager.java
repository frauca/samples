package spring.boot.helloworld.rest.bussiness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import spring.boot.helloworld.rest.model.Customer;
import spring.boot.helloworld.rest.persistence.CustomerRepository;

@Component
public class CustomerManager {

	@Autowired
	CustomerRepository repository;
	
	public Customer save(Customer customer) {
		return repository.save(customer);
	}
	
	
	public Customer getOne(Long id) {
		return repository.findById(id)
				.orElseThrow(()->new RuntimeException(
						String.format("Customer %s could not be found", id))
						);
	}
}
