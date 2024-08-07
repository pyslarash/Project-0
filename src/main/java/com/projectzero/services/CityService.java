package com.projectzero.services;

import com.projectzero.dtos.CityDto;
import com.projectzero.dtos.UserDto;
import com.projectzero.enums.UserType;
import com.projectzero.models.City;
import com.projectzero.models.User;
import com.projectzero.repositories.CityRepo;
import com.projectzero.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final CityRepo cityRepo;
    private final UserRepo userRepo;

    @Autowired
    public CityService(CityRepo cityRepo, UserRepo userRepo){
        this.cityRepo = cityRepo;
        this.userRepo = userRepo;
    }

    // Save city with user association
    public City saveCity(User user, City city) {
        // Check if the city already exists in the database
        Optional<City> existingCity = cityRepo.findByCityAndCountry(city.getCity(), city.getCountry());

        City cityToSave;
        if (existingCity.isPresent()) {
            // If the city exists, use the existing city
            cityToSave = existingCity.get();
        } else {
            // If the city does not exist, save the new city
            cityToSave = cityRepo.save(city);
        }

        // Associate the city with the user
        // Check if the user already has this city associated
        if (!user.getCities().contains(cityToSave)) {
            user.getCities().add(cityToSave);
            userRepo.save(user); // Ensure you save the user to persist the association
        }

        return cityToSave;
    }

    // Save city without user association
    public City saveCity(City city) {
        return cityRepo.save(city);
    }

    public Optional<City> patchCity(Integer id, City city) {
        Optional<City> cityfound = cityRepo.findById(id);
        if (cityfound.isPresent()) {
            City exists = cityfound.get();
            exists.setCity(city.getCity());
            City updated = cityRepo.save(exists);
            return Optional.of(updated);
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<CityDto> getCitiesForUser(User currentUser, Integer userId) {
        // Check if the current user is an admin or if they are trying to view their own cities
        if (currentUser.getType() == UserType.ADMIN || currentUser.getId().equals(userId)) {
            List<City> cities;

            if (currentUser.getType() == UserType.ADMIN) {
                // Admins can view all cities for the specified user
                User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
                cities = List.copyOf(user.getCities());
            } else {
                // Regular users can view only their own cities
                cities = List.copyOf(currentUser.getCities());
            }

            return cities.stream()
                    .map(city -> {
                        CityDto cityDto = new CityDto();
                        cityDto.setId(city.getId());
                        cityDto.setCity(city.getCity());
                        cityDto.setCountry(city.getCountry());

                        if (currentUser.getType() == UserType.ADMIN) {
                            cityDto.setUsers(city.getUsers().stream()
                                    .map(u -> {
                                        UserDto userDto = new UserDto();
                                        userDto.setId(u.getId());
                                        userDto.setLogin(u.getLogin());
                                        userDto.setEmail(u.getEmail());
                                        return userDto;
                                    })
                                    .collect(Collectors.toSet()));
                        } else {
                            cityDto.setUsers(null);
                        }

                        return cityDto;
                    })
                    .collect(Collectors.toList());
        }

        throw new SecurityException("Unauthorized access");
    }


    @Transactional(readOnly = true)
    public List<CityDto> getAllCities(User user) {
        List<City> cities = cityRepo.findAll(); // Fetch all cities for both admins and regular users

        return cities.stream()
                .map(city -> {
                    CityDto cityDto = new CityDto();
                    cityDto.setId(city.getId());
                    cityDto.setCity(city.getCity());
                    cityDto.setCountry(city.getCountry());

                    if (user.getType() == UserType.ADMIN) {
                        // Admins get the users associated with the city
                        cityDto.setUsers(city.getUsers().stream()
                                .map(u -> {
                                    UserDto userDto = new UserDto();
                                    userDto.setId(u.getId());
                                    userDto.setLogin(u.getLogin());
                                    userDto.setEmail(u.getEmail());
                                    return userDto;
                                })
                                .collect(Collectors.toSet()));
                    } else {
                        // Regular users do not get the users associated with the city
                        cityDto.setUsers(null);
                    }

                    return cityDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CityDto> getCityById(User user, Integer id) {
        Optional<City> cityOpt = cityRepo.findById(id);
        if (cityOpt.isPresent()) {
            City city = cityOpt.get();
            CityDto cityDto = new CityDto();
            cityDto.setId(city.getId());
            cityDto.setCity(city.getCity());
            cityDto.setCountry(city.getCountry());
            if (user.getType() == UserType.ADMIN) {
                cityDto.setUsers(city.getUsers().stream()
                        .map(u -> {
                            UserDto userDto = new UserDto();
                            userDto.setId(u.getId());
                            userDto.setLogin(u.getLogin());
                            userDto.setEmail(u.getEmail());
                            return userDto;
                        })
                        .collect(Collectors.toSet()));
            } else {
                cityDto.setUsers(null);
            }
            return Optional.of(cityDto);
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<CityDto> patchCity(User user, Integer id, City city) {
        if (user.getType() == UserType.ADMIN || user.getCities().stream().anyMatch(c -> c.getId().equals(id))) {
            Optional<City> cityOpt = cityRepo.findById(id);
            if (cityOpt.isPresent()) {
                City existingCity = cityOpt.get();
                existingCity.setCity(city.getCity());
                existingCity.setCountry(city.getCountry()); // Update other fields if necessary
                City updatedCity = cityRepo.save(existingCity);

                // Create and return the CityDto with only required fields
                CityDto cityDto = new CityDto();
                cityDto.setId(updatedCity.getId());
                cityDto.setCity(updatedCity.getCity());
                cityDto.setCountry(updatedCity.getCountry());
                return Optional.of(cityDto);
            }
        }
        return Optional.empty();
    }


    @Transactional
    public void deleteCityById(User user, Integer id) {
        Optional<City> cityOptional = cityRepo.findById(id);
        if (cityOptional.isEmpty()) {
            throw new IllegalStateException("City not found");
        }

        City city = cityOptional.get();

        // Admins can delete any city
        if (user.getType() == UserType.ADMIN) {
            // Remove city from all users
            List<User> users = userRepo.findByCitiesContains(city);
            for (User u : users) {
                u.getCities().remove(city);
                userRepo.save(u); // Save user changes
            }
            // Delete the city
            cityRepo.deleteById(id);
        } else if (user.getCities().contains(city)) {
            // Regular users can only delete cities they are associated with
            // Remove the city from their associations
            user.getCities().remove(city);
            userRepo.save(user); // Save user changes
            // Then delete the city
            cityRepo.deleteById(id);
        } else {
            throw new IllegalStateException("User not authorized to delete this city");
        }
    }

    @Transactional(readOnly = true)
    public Optional<CityDto> getCityByCity(User user, String cityName) {
        if (user == null) {
            throw new IllegalArgumentException("User must be authenticated");
        }

        Optional<City> cityOpt = cityRepo.findByCity(cityName);
        if (cityOpt.isPresent()) {
            City city = cityOpt.get();
            CityDto cityDto = new CityDto();
            cityDto.setId(city.getId());
            cityDto.setCity(city.getCity());
            cityDto.setCountry(city.getCountry());

            if (user.getType() == UserType.ADMIN) {
                cityDto.setUsers(city.getUsers().stream()
                        .map(u -> {
                            UserDto userDto = new UserDto();
                            userDto.setId(u.getId());
                            userDto.setLogin(u.getLogin());
                            userDto.setEmail(u.getEmail());
                            return userDto;
                        })
                        .collect(Collectors.toSet()));
            } else {
                cityDto.setUsers(null);
            }
            return Optional.of(cityDto);
        }
        return Optional.empty();
    }
}
