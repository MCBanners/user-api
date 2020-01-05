package com.mcbanners.userapi.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtHandler {
    @Value("${security.jwt.uri:/user/**}")
    private String uri;
    @Value("${security.jwt.header:Authorization}")
    private String header;
    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;
    @Value("${security.jwt.expiration:#{24 * 60 * 60}}")
    private long expiration;
    @Value("${security.jwt.secret:secret}")
    private String secret;

    private SecretKey secretKey;

    @PostConstruct
    public void postConstruct() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(String subject) {
        Claims claims = Jwts.claims().setSubject(subject);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + this.expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(this.secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUri() {
        return uri;
    }

    public String getHeader() {
        return header;
    }

    public String getPrefix() {
        return prefix;
    }

    public long getExpiration() {
        return expiration;
    }

    public String getSecret() {
        return secret;
    }
}
