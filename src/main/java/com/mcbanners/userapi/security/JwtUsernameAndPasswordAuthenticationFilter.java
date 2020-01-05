package com.mcbanners.userapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcbanners.userapi.persistence.repo.UserRepository;
import com.mcbanners.userapi.security.jwt.JwtHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtHandler jwtHandler;

    @Autowired
    public JwtUsernameAndPasswordAuthenticationFilter(UserRepository userRepository, AuthenticationManager authenticationManager, JwtHandler jwtHandler) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtHandler = jwtHandler;

        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/user/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String passedUsername = request.getParameter("username");
        String passedPassword = request.getParameter("password");

        return authenticationManager.authenticate(
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


        com.mcbanners.userapi.persistence.entity.User mcbUser = userRepository.findByUsername(user.getUsername());
        String token = jwtHandler.createToken(user.getUsername());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);

        PrintWriter writer = response.getWriter();
        writer.print(new ObjectMapper().writeValueAsString(new SuccessfulAuthentication(mcbUser, token)));
        writer.flush();
    }

    private static class SuccessfulAuthentication {
        private com.mcbanners.userapi.persistence.entity.User user;
        private String token;

        public SuccessfulAuthentication() {
        }

        public SuccessfulAuthentication(com.mcbanners.userapi.persistence.entity.User user, String token) {
            this.user = user;
            this.token = token;
        }

        public com.mcbanners.userapi.persistence.entity.User getUser() {
            return user;
        }

        public void setUser(com.mcbanners.userapi.persistence.entity.User user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
