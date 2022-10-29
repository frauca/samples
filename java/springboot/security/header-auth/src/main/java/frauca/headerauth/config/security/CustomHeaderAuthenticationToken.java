package frauca.headerauth.config.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomHeaderAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private final String password;

    private CustomHeaderAuthenticationToken(String password) {
        super(null);
        this.password = password;
        setAuthenticated(false);
    }

    public static CustomHeaderAuthenticationToken unAuthenticated(String password){
        return new CustomHeaderAuthenticationToken(password);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return "token";
    }
}
