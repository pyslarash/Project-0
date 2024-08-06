package com.projectzero.services;


import com.projectzero.enums.UserType;
import com.projectzero.models.City;
import com.projectzero.models.User;
import com.projectzero.repositories.CityRepo;
import com.projectzero.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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


    public List<City> getAllCities(User user) {
        if (user.getType() == UserType.ADMIN) {
            return cityRepo.findAll();
        }
        return List.copyOf(user.getCities());
    }

    @Transactional(readOnly = true)
    public Optional<City> getCityById(User user, Integer id) {
        Optional<City> cityOpt = cityRepo.findById(id);
        if (cityOpt.isPresent()) {
            City city = cityOpt.get();
            if (user.getType() == UserType.ADMIN) {
                // For admin users, load the city along with associated users
                city.getUsers().size(); // Trigger loading of users
            } else {
                // Non-admins won't get users associated with the city
                city.setUsers(null);
            }
            return Optional.of(city);
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
