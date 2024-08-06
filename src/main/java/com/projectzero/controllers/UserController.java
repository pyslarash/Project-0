package com.projectzero.controllers;

import com.projectzero.enums.UserType;
import com.projectzero.models.User;
import com.projectzero.services.UserService;
import com.projectzero.services.AuthService;
import com.projectzero.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.projectzero.dtos.UserDto;
import com.projectzero.dtos.CityDto;



import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, AuthService authService, JwtService jwtService) {
        this.userService = userService;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        String message = "Hello, World";
        return ResponseEntity.ok(message);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(@AuthenticationPrincipal User currentUser) {
        // Check if the current user is an admin
        if (currentUser.getType() == UserType.ADMIN) {
            List<UserDto> userDto = userService.getAllUsers();
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id, @AuthenticationPrincipal User currentUser) {
        Optional<UserDto> userDtoOpt = userService.getUserById(currentUser, id);
        return userDtoOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }
}
