package app.security.abstract_users.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@PropertySource("authentication.properties")
@Component
@NoArgsConstructor
public class AbstractUserJwtUtil {

    @Value("${jwt.sign_key}")
    private String KEY;

    public AbstractUserJwtUtil(String key) {
        this.KEY = key;
    }

    public String generateToken(String accountId) {
        return generateToken(accountId, Collections.emptyMap());
    }

    public String generateToken(AbstractUserDetails abstractUserDetails) {
        return generateToken(abstractUserDetails, Collections.emptyMap());
    }

    public String generateToken(AbstractUserDetails abstractUserDetails, Map<String, String> extraClaims) {
        return generateToken(abstractUserDetails.getAccountId(), extraClaims);
    }

    public String generateToken(String accountId, Map<String, String> extraClaims) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(accountId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 3_600_000))
                .signWith(signKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, AbstractUserDetails abstractUserDetails) {
        try {
            String accountId = extractAccountId(token);
            return accountId.equals(abstractUserDetails.getAccountId()) && !isTokenExpired(token);
        } catch (JwtException jwtException) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (JwtException jwtException) {
            return true;
        }
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractAccountId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        token = token.trim();
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Jwts
                .parserBuilder()
                .setSigningKey(signKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key signKey() {
        byte[] bytes = Decoders.BASE64.decode(KEY);
        return Keys.hmacShaKeyFor(bytes);
    }
}
