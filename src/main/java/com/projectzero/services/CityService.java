package com.projectzero.services;


import com.projectzero.dtos.CityDto;
import com.projectzero.enums.UserType;
import com.projectzero.models.City;
import com.projectzero.models.User;
import com.projectzero.repositories.CityRepo;
import com.projectzero.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.projectzero.dtos.UserDto;

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


    @Transactional(readOnly = true)
    public List<CityDto> getAllCities(User user) {
        List<City> cities;
        if (user.getType() == UserType.ADMIN) {
            cities = cityRepo.findAll();
        } else {
            cities = List.copyOf(user.getCities());
        }

        return cities.stream()
                .map(city -> {
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
                                    // You may set cities here if needed
                                    return userDto;
                                })
                                .collect(Collectors.toSet()));
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
                            // You may set cities here if needed
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

    public Optional<City> patchCity(User user, Integer id, City city){
        if (user.getType() == UserType.ADMIN || user.getCities().contains(city)) {
            Optional<City> cityfound = cityRepo.findById(id);
            if (!cityfound.isEmpty()) {
                Optional<City> found = Optional.of(cityRepo.save(city));
                return cityfound;
            }
        }
        return Optional.empty();
    }

    public void deleteCityById(User user, Integer id) {
        Optional<City> city = cityRepo.findById(id);
        if (city.isPresent() && (user.getType() == UserType.ADMIN || user.getCities().contains(city.get()))) {
            cityRepo.deleteById(id);
        } else {
            throw new IllegalStateException("User not authorized to delete this city");
        }
    }
}
