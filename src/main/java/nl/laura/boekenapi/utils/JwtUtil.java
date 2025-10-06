package nl.laura.boekenapi.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    private final String secret;
    private final long expirationMs;

    public JwtUtil(
            @Value("${jwt.secret:CHANGE_ME_TO_A_LONG_RANDOM_SECRET_32+_CHARS}") String secret,
            @Value("${jwt.expiration-ms:864000000}") long expirationMs
    ) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUsername(String token) {
        return parse(token).getSubject();
    }

    public String generateToken(UserDetails user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String usernameIfValid(String token, UserDetails user) {
        try {
            Claims c = parse(token);
            String subject = c.getSubject();
            Date exp = c.getExpiration();
            if (subject == null || exp == null) return null;
            if (!subject.equalsIgnoreCase(user.getUsername())) return null;
            if (exp.before(new Date())) return null;
            return subject;
        } catch (Exception e) {
            return null;
        }
    }

    private Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
