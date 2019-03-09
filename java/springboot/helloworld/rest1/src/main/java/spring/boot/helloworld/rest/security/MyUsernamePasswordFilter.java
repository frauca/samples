package spring.boot.helloworld.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;


public class MyUsernamePasswordFilter extends GenericFilterBean {
	
	private AuthenticationManager authenticationManger;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	
	public MyUsernamePasswordFilter(AuthenticationManager authenticationManger) {
		this.authenticationManger = authenticationManger;
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String userName=request.getParameter("username");
		String password=request.getParameter("password");
		logger.info(String.format("Try to authenticate %s::%s", userName,password));
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName, password);
        // dump token into security context (for authentication-provider to pick up)
        Authentication auth = authenticationManger
                .authenticate(authRequest);
        logger.info(String.format("Authentication --> %s", auth));
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
	}
	
}
