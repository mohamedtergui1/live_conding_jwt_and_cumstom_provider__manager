package ma.tr.livr_coding.security;

import ma.tr.livr_coding.service.jwt.JwtServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


public record JwtAuthProvider(JwtServiceImpl jwtService,
                              UserDetailsService userDetailsService) implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        if(!jwtService.isAccessToken(token)){
            throw new BadCredentialsException("Bad credentials");
        }

        String email = jwtService.extractEmail(token);
        if (email == null) {
            throw new BadCredentialsException("Invalid token");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (jwtService.isTokenValid(token, userDetails)) {
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        throw new BadCredentialsException("Invalid token");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
