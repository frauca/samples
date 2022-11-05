package frauca.oauth.config;

import frauca.oauth.config.custom.CustomHeaderAuthenticationProvider;
import frauca.oauth.config.custom.CustomHeaderFilter;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MultipleAuthConfiguration {

    @Bean
    SecurityFilterChain multipleAuths(HttpSecurity http, Environment env) throws Exception {
        val user1 = new CustomHeaderAuthenticationProvider(env.getProperty("custom.user1"));
        val user2 = new CustomHeaderAuthenticationProvider(env.getProperty("custom.user2"));
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .and()
                .addFilterBefore(new CustomHeaderFilter(user1), OAuth2AuthorizationRequestRedirectFilter.class)
                .addFilterBefore(new CustomHeaderFilter(user2), OAuth2AuthorizationRequestRedirectFilter.class);
         return   http.build();
    }


}
