package com.mcbanners.userapi.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager manager;
    private final JwtProvider provider;

    public JwtAuthenticationFilter(AuthenticationManager manager, JwtProvider provider) {
        this.manager = manager;
        this.provider = provider;

        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/user/sign-in", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String passedUsername = request.getParameter("username");
        String passedPassword = request.getParameter("password");

        return manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        passedUsername,
                        passedPassword,
                        new ArrayList<>()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String token = provider.createToken(user.getUsername());
        response.addHeader(JwtProvider.HEADER_NAME, JwtProvider.TOKEN_PREFIX + token);
    }
}
