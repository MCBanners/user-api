package com.mcbanners.userapi.controller;

import com.mcbanners.userapi.entity.User;
import com.mcbanners.userapi.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        User user = repository.findByUsernameOrEmail(username, email);
        if (user != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with that username or email already exists.");
        }


        user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
        user = repository.save(user);


        if (repository.existsById(user.getId())) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to create new user.");
        }
    }
}
