package ma.tr.livr_coding.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private Object principle;
    public String token;

    public JwtAuthenticationToken(String token) {
        super(null);
        setAuthenticated(false);
        this.token = token;
    }

    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principle;
    }

}

























