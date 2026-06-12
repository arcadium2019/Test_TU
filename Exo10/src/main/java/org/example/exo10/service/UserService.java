package org.example.exo10.service;

import org.example.exo10.model.User;
import org.example.exo10.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createAccount(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        User user = new User(username, email, password);
        userRepository.save(user);
        return user;
    }

    public User login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new LoginFailedException("Invalid credentials"));
    }
}
