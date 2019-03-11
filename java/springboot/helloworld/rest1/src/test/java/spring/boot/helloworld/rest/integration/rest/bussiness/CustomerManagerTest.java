package spring.boot.helloworld.rest.integration.rest.bussiness;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import spring.boot.helloworld.rest.RestApplicationTests;
import spring.boot.helloworld.rest.bussiness.CustomerManager;
import spring.boot.helloworld.rest.model.Customer;

public class CustomerManagerTest extends RestApplicationTests {
	
	@Autowired
	CustomerManager customerManager;
	
	@Test
	public void just_find_pre_loaded_user_1() {
	    // given
	    Customer roger = customerManager.getOne(1l);
	 
	    // then
	    assertThat(roger.getName())
	      .isEqualTo("roger");
	}
}
