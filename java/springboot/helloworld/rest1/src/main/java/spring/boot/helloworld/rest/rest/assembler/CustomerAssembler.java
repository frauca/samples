package spring.boot.helloworld.rest.rest.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import spring.boot.helloworld.rest.model.Customer;
import spring.boot.helloworld.rest.rest.CustomerController;

@Component
public class CustomerAssembler implements ResourceAssembler<Customer, Resource<Customer>> {

	@Override
	public Resource<Customer> toResource(Customer entity) {
		return new Resource<Customer>(entity, 
				linkTo(methodOn(CustomerController.class).one(entity.getId())).withSelfRel());
	}
	
}
