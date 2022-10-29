package frauca.headerauth.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
@Slf4j
public class CustomHeaderAuthenticationProvider implements AuthenticationProvider {

    private final String password;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication instanceof CustomHeaderAuthenticationToken token){
            log.info("Check password");
            if(password.equals(token.getPassword())) {
                token.setAuthenticated(true);
                return token;
            }
            throw new InvalidPasswordException("Provided password is not the correct one");
        }
        log.warn("Invalid token type has been used "
                +authentication==null?"null":authentication.getClass().getSimpleName());
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication!=null
                &&authentication.isAssignableFrom(CustomHeaderAuthenticationToken.class);
    }
}
