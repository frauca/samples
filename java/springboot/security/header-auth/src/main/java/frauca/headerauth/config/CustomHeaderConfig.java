package frauca.headerauth.config;

import frauca.headerauth.config.security.CustomHeaderAuthenticationProvider;
import frauca.headerauth.config.security.CustomHeaderFilter;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class CustomHeaderConfig{

    @Bean
    public SecurityFilterChain addCustomFilter(HttpSecurity http,
                          AuthenticationManager authenticationManager) throws Exception {
        val filter = new CustomHeaderFilter(authenticationManager);
        return http
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(filter, BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       @Value("${password}") String password) throws Exception {
        val authenticationProvider = new CustomHeaderAuthenticationProvider(password);
        val authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        return authBuilder
                .parentAuthenticationManager(null)
                .authenticationProvider(authenticationProvider)
                .build();
    }
}
