package frauca.headerauth.config.security;

import org.springframework.security.core.AuthenticationException;

public class NoPasswordFoundException extends AuthenticationException {
    public NoPasswordFoundException(String msg) {
        super(msg);
    }
}
