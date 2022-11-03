package frauca.oauth.config.custom;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class CustomHeaderFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Custom header filter "+((HttpServletRequest) request).getServletPath());
        authenticate();
        chain.doFilter(request,response);
    }

    private void authenticate(){
        val context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new CustomHeaderToken());
        SecurityContextHolder.setContext(context);
    }
}
