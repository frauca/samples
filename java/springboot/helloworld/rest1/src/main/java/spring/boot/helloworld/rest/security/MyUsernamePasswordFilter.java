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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import lombok.extern.slf4j.Slf4j;


public class MyUsernamePasswordFilter extends GenericFilterBean {
	
	private AuthenticationManager authenticationManger;

	
	public MyUsernamePasswordFilter(AuthenticationManager authenticationManger) {
		this.authenticationManger = authenticationManger;
	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		try {
			if(requiresAuthentication(request, response)) {
				authenticate(request, response);
			}
			chain.doFilter(request, response);
		}catch (Exception e) {
			logger.error(String.format("Internal Error on Authentication:: %s", e.getMessage()),e);
			unsussessfullAuthentication(request, response);
		}
		
        
	}
	
	
	
	protected void unsussessfullAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.getReasonPhrase());
	}
	
	protected void authenticate(HttpServletRequest request, HttpServletResponse response) {
		String userName=getUsername(request);
		String password=getPassword(request);
		logger.info(String.format("Try to authenticate %s::%s", userName,password));
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName, password);
        // dump token into security context (for authentication-provider to pick up)
        Authentication auth = authenticationManger
                .authenticate(authRequest);
        logger.info(String.format("Authentication --> %s", auth));
        SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		return !isAlreadyLoged()&&hasEnoguthDataToAuthenticate(request);
	}
	
	protected boolean isAlreadyLoged() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return !(auth==null||!auth.isAuthenticated());
	}
	
	protected boolean hasEnoguthDataToAuthenticate(HttpServletRequest request) {
		return !StringUtils.isEmpty(getUsername(request))
				&&!StringUtils.isEmpty(getPassword(request));
	}
	
	protected String getUsername(HttpServletRequest request) {
		return request.getParameter("username");
	}
	
	protected String getPassword(HttpServletRequest request) {
		return request.getParameter("password");
	}
}
