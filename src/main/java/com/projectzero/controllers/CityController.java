package com.projectzero.controllers;

import com.projectzero.enums.UserType;
import com.projectzero.models.City;
import com.projectzero.models.User;
import com.projectzero.services.CityService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.projectzero.repositories.CityRepo;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/city")
public class CityController {

    private final CityService cityService;

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping("/")
    public ResponseEntity<City> postCity(@RequestBody City city, @AuthenticationPrincipal User user){
        City saveCity = cityService.saveCity(user, city);
        return ResponseEntity.ok(saveCity);
    }

    @GetMapping("/")
    public  ResponseEntity<List<City>> getCities(@AuthenticationPrincipal User user){
        List<City> cities = cityService.getAllCities(user);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        Optional<City> city = cityService.getCityById(user, id);
        return city.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<City> updateCityById(@PathVariable Integer id,@RequestBody City city, @AuthenticationPrincipal User user){
        Optional<City> upCity = cityService.patchCity(user, id, city);
        return upCity.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Integer id, @AuthenticationPrincipal User user){
        cityService.deleteCityById(user, id);
        return ResponseEntity.noContent().build();
    }
}
