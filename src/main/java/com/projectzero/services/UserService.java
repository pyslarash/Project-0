package com.projectzero.services;

import com.projectzero.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.projectzero.repositories.UserRepo;
import com.projectzero.models.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepo userRepo;
    PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepo.findById(id);
    }

    public User saveUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepo.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String login(String login, String password) {
        // Try to find the user by login
        Optional<User> userOpt = userRepo.findByLogin(login);

        if (userOpt.isEmpty()) {
            return null; // User not found
        }

        User user = userOpt.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(login);
        } else {
            return null;
        }
    }

}
