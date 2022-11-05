package frauca.oauth.config.custom;

import lombok.NonNull;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public class CustomHeaderAuthenticationProvider {

    public static final String HEADER_PASSWORD_ATT = "Custom-User-Password";
    private final String password;


    public CustomHeaderAuthenticationProvider(@NonNull String password) {
        this.password = password;
    }

    public Authentication authenticate(HttpServletRequest request){
        String providedPassword = request.getHeader(HEADER_PASSWORD_ATT);
        if(!password.equals(providedPassword)){
            return null;
        }
        return new CustomHeaderToken();
    }
}
