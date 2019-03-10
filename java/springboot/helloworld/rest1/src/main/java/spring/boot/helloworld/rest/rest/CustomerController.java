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
import spring.boot.helloworld.rest.rest.assembler.CustomerAssembler;

@RestController
@Slf4j
public class CustomerController {
	
	@Autowired
	CutomerService customerService;
	
	@Autowired
	CustomerAssembler assembler;

	@PostMapping("/customeers")
	public Resource<Customer> newCustomer(@RequestBody Customer customer) {
		log.info(String.format("Try to add user :: %s", customer));
		Customer newCustomer = customerService.save(customer);
		return assembler.toResource(newCustomer);
	}
	
	@GetMapping("/customeers/{id}")
	public Resource<Customer> one(@PathVariable Long id) {
		Customer customer = customerService.getOne(id);
		return assembler.toResource(customer);
	}
}
