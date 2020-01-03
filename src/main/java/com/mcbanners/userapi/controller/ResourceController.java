package com.mcbanners.userapi.controller;

import com.mcbanners.userapi.persistence.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("resource")
public class ResourceController {
    private final UserRepository users;

    @Autowired
    public ResourceController(UserRepository users) {
        this.users = users;
    }
    
    @GetMapping(value = "available/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Boolean>> usernameAvailable(@PathVariable String username) {
        return new ResponseEntity<>(
                Collections.singletonMap("available", !users.existsByUsername(username)),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "available/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Boolean>> emailAvailable(@PathVariable String email) {
        return new ResponseEntity<>(
                Collections.singletonMap("available", !users.existsByEmail(email)),
                HttpStatus.OK
        );
    }
}
