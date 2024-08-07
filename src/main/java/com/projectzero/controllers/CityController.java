package com.projectzero.controllers;

import com.projectzero.dtos.CityDto;
import com.projectzero.enums.UserType;
import com.projectzero.models.City;
import com.projectzero.models.User;
import com.projectzero.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/city")
public class CityController {

    CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping("/")
    public ResponseEntity<City> postCity(@RequestBody City city, @AuthenticationPrincipal User user) {
        // Validate city input
        if (city.getCity() == null || city.getCountry() == null || city.getCity().trim().isEmpty() || city.getCountry().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Use user to check if the city exists
        Optional<CityDto> cityDtoOpt = cityService.getCityByCity(user, city.getCity());
        if (cityDtoOpt.isPresent()) {
            CityDto cityDto = cityDtoOpt.get();
            if (Objects.equals(cityDto.getCity(), city.getCity()) &&
                    Objects.equals(cityDto.getCountry(), city.getCountry())) {
                return ResponseEntity.badRequest().build();
            }
        }

        // Save the new city
        City savedCity = cityService.saveCity(user, city);
        return ResponseEntity.ok(savedCity);
    }

    @GetMapping("/name/{cityName}")
    public ResponseEntity<CityDto> getCityByCity(@PathVariable String cityName, @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<CityDto> cityDto = cityService.getCityByCity(user, cityName);
        return cityDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CityDto>> getCitiesForUser(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Integer userId) {
        try {
            List<CityDto> cities = cityService.getCitiesForUser(currentUser, userId);
            return ResponseEntity.ok(cities);
        } catch (SecurityException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/")
    public ResponseEntity<List<CityDto>> getCities(@AuthenticationPrincipal User currentUser) {
        List<CityDto> cities = cityService.getAllCities(currentUser);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCityById(@PathVariable Integer id, @AuthenticationPrincipal User currentUser) {
        Optional<CityDto> city = cityService.getCityById(currentUser, id);
        return city.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CityDto> updateCityById(@PathVariable Integer id, @RequestBody City city, @AuthenticationPrincipal User user) {
        // Validate city input
        if (id == null || id <= 0 || city == null || city.getCity() == null || city.getCountry() == null ||
                city.getCity().trim().isEmpty() || city.getCountry().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<CityDto> existingCityDto = cityService.getCityById(user, id);
        if (existingCityDto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<CityDto> updatedCityDto = cityService.patchCity(user, id, city);
        return updatedCityDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            cityService.deleteCityById(user, id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).build(); // Forbidden if the user is not authorized
        }
    }

}
