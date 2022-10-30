package frauca.headerauth.config.security;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.util.OnCommittedResponseWrapper;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomHeaderFilter extends AbstractAuthenticationProcessingFilter  {

    public static final AntPathRequestMatcher REQUEST_MATCHER = new AntPathRequestMatcher("/**");
    public static final String HEADER_PASSWORD_NAME = "Custom-Password";

    public CustomHeaderFilter(AuthenticationManager authenticationManager) {
        super(REQUEST_MATCHER, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        val password = request.getHeader(HEADER_PASSWORD_NAME);
        if(!StringUtils.hasText(password)) {
            log.info("Password information not present on header "+request.getServletPath());
            throw new NoPasswordFoundException("The request has no %s header present".formatted(HEADER_PASSWORD_NAME));
        }
        log.info("Try to authenticate request "+request.getServletPath());
        val authenticationRequest = CustomHeaderAuthenticationToken.unAuthenticated(password);
        return getAuthenticationManager().authenticate(authenticationRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request,response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
