package app.security.config;

import app.security.user.model.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
public class JwtUtil {
    @Value("${jwt.sign_key}")
    private String KEY;
    private Key signKey(){
        byte[] bytes = Decoders.BASE64.decode(KEY);
        return Keys.hmacShaKeyFor(bytes);
    }
    public String generateToken(UserDetails userDetails, Map<String,String> extraClaims){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+3_600_000))
                .signWith(signKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateToken(UserDetails userDetails){
        return generateToken(userDetails, Collections.emptyMap());
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }
    private Claims extractAllClaims(String token){
        if(token.startsWith("Bearer "))
            token = token.substring(7);
        return Jwts
                .parserBuilder()
                .setSigningKey(signKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
//TODO перределать JwtUtil под нормальную работу с токенами с Bearer и без
