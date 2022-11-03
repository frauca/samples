package frauca.oauth.config;

import frauca.oauth.config.custom.CustomHeaderFilter;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class MultipleAuthConfiguration {

    @Bean
    SecurityFilterChain multipleAuths(HttpSecurity http) throws Exception {
        val authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .and()
                .addFilterBefore(new CustomHeaderFilter(), OAuth2AuthorizationRequestRedirectFilter.class);
         return   http.build();
    }
}
