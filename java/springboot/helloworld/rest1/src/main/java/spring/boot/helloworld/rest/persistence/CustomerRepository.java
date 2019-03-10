package spring.boot.helloworld.rest.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import spring.boot.helloworld.rest.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	
}
	