package frauca.oauth.config.custom;

import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class CustomHeaderAuthenticationProvider {

    public static final String HEADER_PASSWORD_ATT = "Custom-User-Password";
    private final String password;
    private final List<? extends GrantedAuthority> roles;


    public CustomHeaderAuthenticationProvider(@NonNull String password, @NonNull List<? extends GrantedAuthority> roles) {
        this.password = password;
        this.roles = roles;
    }

    public Authentication authenticate(HttpServletRequest request){
        String providedPassword = request.getHeader(HEADER_PASSWORD_ATT);
        if(!password.equals(providedPassword)){
            return null;
        }
        return new CustomHeaderToken(roles);
    }
}
