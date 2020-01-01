package com.mcbanners.userapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtProvider {
    public static final String HEADER_NAME = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretPhrase = "secret";

    @Value("${security.jwt.token.validity-length:3600000}")
    private long validityLength = 3_600_000L;

    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(secretPhrase.getBytes());
    }

    public String createToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityLength);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public UsernamePasswordAuthenticationToken validate(String token) {
        if (token != null) {
            Claims parsed;

            try {
                parsed = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            } catch (Exception ignored) {
                return null;
            }

            if (parsed != null) {
                return new UsernamePasswordAuthenticationToken(parsed.getSubject(), null, new ArrayList<>());
            }
        }

        return null;
    }
}
