package spring.boot.helloworld.rest.bussiness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.boot.helloworld.rest.model.Customer;


@Service
public class CutomerService {

	@Autowired
	CustomerManager customerManager;
	
	public Customer save(Customer customer) {
		return customerManager.save(customer);
	}
}
