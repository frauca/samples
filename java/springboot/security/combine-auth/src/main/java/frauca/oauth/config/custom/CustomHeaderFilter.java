package frauca.oauth.config.custom;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class CustomHeaderFilter extends GenericFilterBean {

    private final RequestMatcher matcher = new NegatedRequestMatcher(
            new AntPathRequestMatcher("/**/oauth2/**")
    );
    private final CustomHeaderAuthenticationProvider authenticator;

    public CustomHeaderFilter(CustomHeaderAuthenticationProvider authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Custom header filter " + ((HttpServletRequest) request).getServletPath());
        if (request instanceof HttpServletRequest httpRequest && matcher.matches(httpRequest) && !isAuthenticated()) {
            authenticate(httpRequest);
        }
        chain.doFilter(request, response);
        cleanIfNeeded();
    }

    private void authenticate(HttpServletRequest request) {
        val user = authenticator.authenticate(request);
        if (user != null) {
            log.info("User has been authenticated for " + request.getServletPath());
            val context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(user);
            SecurityContextHolder.setContext(context);
        } else {
            log.info("Could not authenticate user on " + request.getServletPath());
        }
    }

    private void cleanIfNeeded() {
        Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .ifPresent(auth -> {
                    if (auth instanceof CustomHeaderToken) {
                        SecurityContextHolder.clearContext();
                    }
                });
    }

    private boolean isAuthenticated() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::isAuthenticated)
                .orElse(false);
    }
}
