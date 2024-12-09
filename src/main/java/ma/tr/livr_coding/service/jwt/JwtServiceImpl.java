package ma.tr.livr_coding.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl   {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.access-token-expiration:30000}")
    private Long accessTokenExpiration;

    @Value("${security.jwt.refresh-token-expiration:70000000}")
    private Long refreshTokenExpiration;


    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

    }


    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, accessTokenExpiration, "ACCESS");
    }


    public String generateTokenRefresh(UserDetails userDetails) {
        return generateTokenRefresh(new HashMap<>(), userDetails);
    }


    public String generateTokenRefresh(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, refreshTokenExpiration, "REFRESH");
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            Long expiration,
            String tokenType
    ) {
        Date now = new Date();
        extraClaims.put("token_type", tokenType);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public long getExpirationTime() {
        return accessTokenExpiration;
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractEmail(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractTokenType(String token) {
        try {
            return extractClaim(token, claims -> claims.get("token_type", String.class));
        } catch (Exception e) {
            return null;
        }
    }

    // Additional method to check if a token is a refresh token
    public boolean isRefreshToken(String token) {
        String tokenType = extractTokenType(token);
        return "REFRESH".equals(tokenType);
    }

    // Additional method to check if a token is an access token
    public boolean isAccessToken(String token) {
        String tokenType = extractTokenType(token);
        return "ACCESS".equals(tokenType);
    }
}