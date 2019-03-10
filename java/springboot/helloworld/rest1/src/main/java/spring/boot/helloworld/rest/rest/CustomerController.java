package spring.boot.helloworld.rest.rest;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import spring.boot.helloworld.rest.bussiness.CutomerService;
import spring.boot.helloworld.rest.model.Customer;

@RestController
@Slf4j
public class CustomerController {
	
	@Autowired
	CutomerService customerService;

	@PostMapping("/customeers")
	Resource<Customer> newCustomer(@RequestBody Customer customer) {
		log.info(String.format("Try to add user :: %s", customer));
		Customer newCustomer = customerService.save(customer);
		return new Resource<Customer>(newCustomer, 
				linkTo(methodOn(CustomerController.class).one(newCustomer.getId())).withSelfRel());
	}
	
	@GetMapping("/customeers/{id}")
	Resource<Customer> one(@PathVariable Long id) {

		return null;
	}
}
