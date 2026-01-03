package com.mrakshith21.programming_judge.controllers;

import com.mrakshith21.programming_judge.models.User;
import com.mrakshith21.programming_judge.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping
    public List<User> getAllUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return usersService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return usersService.createUser(user);
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User userDetails) {
        try {
            return ResponseEntity.ok(usersService.updateUser(username, userDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
