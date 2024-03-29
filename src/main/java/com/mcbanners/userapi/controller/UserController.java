package com.mcbanners.userapi.controller;

import com.mcbanners.userapi.persistence.entity.User;
import com.mcbanners.userapi.persistence.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserController(PasswordEncoder encoder, UserRepository repository) {
        this.encoder = encoder;
        this.repository = repository;
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        User user = repository.findByUsernameOrEmail(username, email);
        if (user != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with that username or email already exists.");
        }


        user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user = repository.save(user);


        if (repository.existsById(user.getId())) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to create new user.");
        }
    }

    @GetMapping(value = "/resource/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Boolean>> usernameAvailable(@PathVariable String username) {
        return new ResponseEntity<>(
                Collections.singletonMap("available", !repository.existsByUsername(username)),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/resource/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Boolean>> emailAvailable(@PathVariable String email) {
        return new ResponseEntity<>(
                Collections.singletonMap("available", !repository.existsByEmail(email)),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/session/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Boolean>> validate() {
        // basically, if this method gets called then the session is valid; it would get stopped earlier in the pipeline otherwise
        // the body is not necessary, but is consistent with other validity checks throughout the project
        return new ResponseEntity<>(Collections.singletonMap("valid", true), HttpStatus.OK);
    }
}
