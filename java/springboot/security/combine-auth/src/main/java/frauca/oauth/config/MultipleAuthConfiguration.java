package frauca.oauth.config;

import frauca.oauth.config.custom.CustomHeaderAuthenticationProvider;
import frauca.oauth.config.custom.CustomHeaderFilter;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Stream;

@Configuration
public class MultipleAuthConfiguration {

    public static final String USER1_ROLE = "USER1";
    public static final String USER2_ROLE = "USER2";

    @Bean
    SecurityFilterChain multipleAuths(HttpSecurity http, Environment env) throws Exception {
        val user1 = provider(env.getProperty("custom.password1"), USER1_ROLE);
        val user2 = provider(env.getProperty("custom.password2"), USER1_ROLE);
        http
                .authorizeRequests(
                        auth -> auth
                                .antMatchers("/app").authenticated()
                                .antMatchers("/user1").hasRole(USER1_ROLE)
                                .antMatchers("/user2").hasRole(USER2_ROLE)
                )
                .oauth2Login()
                .and()
                .addFilterBefore(new CustomHeaderFilter(user1), OAuth2AuthorizationRequestRedirectFilter.class)
                .addFilterBefore(new CustomHeaderFilter(user2), OAuth2AuthorizationRequestRedirectFilter.class);
        return http.build();
    }

    private CustomHeaderAuthenticationProvider provider(String password, String... rolesNames){
        val roles = Stream.of(rolesNames)
                .map(name -> new SimpleGrantedAuthority("ROLE_"+name))
                .toList();
        return new CustomHeaderAuthenticationProvider(password,roles);
    }


}
