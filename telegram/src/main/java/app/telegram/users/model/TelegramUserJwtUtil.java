package app.telegram.users.model;

import io.jsonwebtoken.*;
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

@PropertySource("classpath:authentication.properties")
@Component
@NoArgsConstructor
public class TelegramUserJwtUtil {

    public final static int EXPIRATION_TIME = 3_600_000;

    @Value("${jwt.sign_key}")
    private String KEY;

    public TelegramUserJwtUtil(String key) {
        this.KEY = key;
    }

    public String generateToken(Long chatId) {
        return generateToken(chatId, Collections.emptyMap());
    }

    public String generateToken(Long chatId, Map<String, String> extraClaims) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(String.valueOf(chatId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRATION_TIME))
                .signWith(signKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValidAndContainsChatId(String token, Long expectedChatId) {
        try {
            String chatId = extractChatId(token);
            return chatId.equals(String.valueOf(expectedChatId)) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractChatId(String token) {
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
