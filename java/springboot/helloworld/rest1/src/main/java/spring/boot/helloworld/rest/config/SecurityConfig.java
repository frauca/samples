package spring.boot.helloworld.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import spring.boot.helloworld.rest.security.MyBasicEntryPoint;
import spring.boot.helloworld.rest.security.MyUsernamePasswordFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	MyBasicEntryPoint myEntryPoint;
	
	

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
	    auth.inMemoryAuthentication()
	        .withUser("user").password(encoder().encode("password")).roles("USER");
	}
	
	@Bean
	public PasswordEncoder  encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception { 
	    http
	    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    .and()
	    .csrf().disable()
	    .exceptionHandling()
	    //.authenticationEntryPoint(myEntryPoint)
	    .and()
	    .authorizeRequests()
	    .antMatchers(HttpMethod.POST,"/customeers").permitAll()
	    .anyRequest().authenticated();

	    http.addFilterBefore(myUsernamePasswordFileter(), UsernamePasswordAuthenticationFilter.class);
	   }
	
	protected MyUsernamePasswordFilter myUsernamePasswordFileter() throws Exception {
		return new MyUsernamePasswordFilter(authenticationManager());
	}
}
