package com.projectzero.services;

import com.projectzero.enums.UserType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.projectzero.repositories.UserRepo;
import com.projectzero.models.User;
import com.projectzero.dtos.UserDto;
import com.projectzero.dtos.CityDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        // Fetch all users with cities eagerly loaded
        List<User> users = userRepo.findAll();

        return users.stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setLogin(user.getLogin());
                    userDto.setEmail(user.getEmail());
                    userDto.setCities(user.getCities().stream()
                            .map(city -> new CityDto(city.getId(), city.getCity(), city.getCountry()))
                            .collect(Collectors.toSet())); // Collect cities to Set
                    return userDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(User currentUser, Integer id) {
        // Fetch the user with cities eagerly loaded
        Optional<User> userOpt = userRepo.findById(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check if the current user is an admin or if they are trying to view their own information
            if (currentUser.getType() == UserType.ADMIN || currentUser.getId().equals(id)) {
                UserDto userDTO = new UserDto();
                userDTO.setId(user.getId());
                userDTO.setLogin(user.getLogin());
                userDTO.setEmail(user.getEmail());
                userDTO.setCities(user.getCities().stream()
                        .map(city -> new CityDto(city.getId(), city.getCity(), city.getCountry()))
                        .collect(Collectors.toSet())); // Collect cities to Set

                // Log city size for debugging
                System.out.println("User cities size: " + user.getCities().size());

                return Optional.of(userDTO);
            }
        }

        return Optional.empty();
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
}
