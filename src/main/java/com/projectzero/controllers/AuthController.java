package com.projectzero.controllers;

import com.projectzero.dtos.LoginUserDto;
import com.projectzero.dtos.RegisterUserDto;
import com.projectzero.models.User;
import com.projectzero.services.AuthService;
import com.projectzero.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        if (registerUserDto.getEmail() == null || registerUserDto.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email cannot be null or empty");
        }
        try {
            // Log the received registration data
            System.out.println("Registering user with email: " + registerUserDto.getEmail());
            authService.signup(registerUserDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            // Log the exception details
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Iterate through the field errors and build a custom error message
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            if ("type".equals(error.getField())) {
                errorMessage.append("Invalid type");
            } else {
                errorMessage.append(error.getDefaultMessage()).append(" ");
            }
        });
        return ResponseEntity.badRequest().body(errorMessage.toString().trim());
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginUserDto loginUserDto) {
        String token = authService.authenticate(loginUserDto);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid login or password"));
        }
        return ResponseEntity.ok(Map.of("token", token));
    }
}
