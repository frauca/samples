package spring.boot.helloworld.rest.unit.rest;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import spring.boot.helloworld.rest.rest.GreetingRest;

@RunWith(SpringRunner.class)
@WebMvcTest(GreetingRest.class)
public class GreetingRestTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Test
    public void greetingShouldReturnMessageFromService() throws Exception {
        this.mockMvc.perform(get("/greeting?name=Roger")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, Roger")));
    }
}
