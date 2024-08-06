package com.projectzero.services;

import com.projectzero.dtos.LoginUserDto;
import com.projectzero.dtos.RegisterUserDto;
import com.projectzero.models.User;
import com.projectzero.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void signup(RegisterUserDto registerUserDto) {
        User user = new User();

        // Set fields from DTO to the User entity
        user.setLogin(registerUserDto.getLogin());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        user.setType(registerUserDto.getType()); // Ensure type is included in DTO

        // Save the User entity
        userRepo.save(user);
    }

    public String authenticate(LoginUserDto loginUserDto) {
        Optional<User> userOpt = userRepo.findByLogin(loginUserDto.getLogin());
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();
        if (passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) {
            return jwtService.generateToken(user);
        } else {
            return null;
        }
    }
}
