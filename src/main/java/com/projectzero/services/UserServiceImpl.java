package com.projectzero.services;

import com.projectzero.models.User;
import com.projectzero.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long id) { // Change int to Long
        Long currentUserId = getCurrentUserId();
        User currentUser = userRepo.findById(currentUserId).orElse(null);

        if (currentUser != null && currentUser.getId().equals(id)) {
            return userRepo.findById(id).orElse(null);
        } else {
            throw new AccessDeniedException("You do not have permission to access this resource.");
        }
    }

    @Override
    public List<User> getAllUsers() {
        Long currentUserId = getCurrentUserId();
        if (isAdmin(currentUserId)) {
            return userRepo.findAll();
        } else {
            throw new AccessDeniedException("You do not have permission to access this resource.");
        }
    }

    @Override
    public User addUser(User user, String rawPassword) {
        if (user == null || rawPassword == null) {
            throw new IllegalArgumentException("User or password cannot be null");
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(hashedPassword);

        // Ensure user type is set properly
        if (user.getType() == null || user.getType().isEmpty()) {
            user.setType("user"); // Default type, change as needed
        }

        // Save the user and return the saved entity
        return userRepo.save(user);
    }

    @Override
    public boolean authenticateUser(String login, String rawPassword) {
        User user = userRepo.findByLogin(login);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public boolean isAdmin(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        return user != null && "admin".equals(user.getType());
    }

    @Override
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Return a UserDetails object for Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                new ArrayList<>()); // Pass user roles and authorities if needed
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Assuming UserDetails has a method to get the user ID
            return ((CustomUserDetails) userDetails).getUserId();
        }
        return null;
    }
}
