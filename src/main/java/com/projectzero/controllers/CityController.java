package com.projectzero.controllers;

import com.projectzero.models.City;
import com.projectzero.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

    CityService cityService;

    @Autowired
    public CityController( CityService cityService){this.cityService = cityService;}

    @PostMapping("/")
    public ResponseEntity<City> postCity(@RequestBody City city){
        City saveCity = cityService.saveCity(city);
        return ResponseEntity.ok(saveCity);
    }

    @GetMapping("/")
    public  ResponseEntity<List<City>> getCities(){
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

}
