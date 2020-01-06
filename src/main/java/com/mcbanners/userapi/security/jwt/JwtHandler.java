package com.mcbanners.userapi.security.jwt;

import com.mcbanners.userapi.persistence.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtHandler {
    @Value("${security.jwt.expiration:#{24 * 60 * 60 * 1000}}")
    private long expiration;
    @Value("${security.jwt.secret:secret}")
    private String secret;

    private SecretKey secretKey;

    @PostConstruct
    public void postConstruct() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("id", user.getId().toString());

        Date now = new Date();
        Date expiration = new Date(now.getTime() + this.expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(this.secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
}
