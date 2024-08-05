package com.projectzero.controllers;

import com.projectzero.models.City;
import com.projectzero.services.CityService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Integer id){
        Optional<City> city = cityService.getCityById(id);
        return city.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PatchMapping("/{id}")
    public ResponseEntity<City> updateCytyById(@PathVariable Integer id,@RequestBody City city){
        Optional<City> upCity = cityService.patchCity(id,city);
        return upCity.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Integer id){
        cityService.deleteCityById(id);
        return ResponseEntity.noContent().build();
    }

}
