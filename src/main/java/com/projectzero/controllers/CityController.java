package com.projectzero.controllers;

import com.projectzero.dtos.CityDto;
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

    @GetMapping("/{cityName}")
    public ResponseEntity<CityDto> getCityByCity(@PathVariable String cityName, @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<CityDto> cityDto = cityService.getCityByCity(user, cityName);
        return cityDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<City>> getCities() {
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Integer id){
        Optional<City> city = cityService.getCityById(id);
        return city.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<City> updateCityById(@PathVariable Integer id, @RequestBody City city, @AuthenticationPrincipal User user) {
        // Validate city input
        if (id == null || id <= 0 || city == null || city.getCity() == null || city.getCountry() == null ||
                city.getCity().trim().isEmpty() || city.getCountry().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<CityDto> existingCityDto = cityService.getCityById(user, id);
        if (existingCityDto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<City> updatedCity = cityService.patchCity(user, id, city);
        return updatedCity.map(ResponseEntity::ok)
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
