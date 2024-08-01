package com.projectzero.services;

import com.projectzero.models.User;
import java.util.List;

public interface UserService {
    User getUserById(Long id); // Change int to Long
    List<User> getAllUsers();
    User addUser(User user, String rawPassword);
    boolean authenticateUser(String login, String rawPassword);
    boolean isAdmin(String login);
}
