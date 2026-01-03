package com.mrakshith21.programming_judge.services;

import com.mrakshith21.programming_judge.models.User;
import com.mrakshith21.programming_judge.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findById(username);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String username, User userDetails) {
        User user = userRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(userDetails.getPassword());
        return userRepository.save(user);
    }
}
