package com.projectzero.controllers;

import com.projectzero.models.City;
import com.projectzero.services.CityService;
import jakarta.validation.constraints.Null;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/city")
public class CityController {

    CityService cityService;

    @Autowired
    public CityController( CityService cityService){this.cityService = cityService;}

    @PostMapping("/")
    public ResponseEntity<City> postCity(@RequestBody City city){
        Optional<City> isInDB = cityService.getCityByCity(city.getCity());
        if(isInDB.isPresent() && Objects.equals(isInDB.get().getCity(), city.getCity())
                && Objects.equals(isInDB.get().getCountry(), city.getCountry())){
           return ResponseEntity.badRequest().build();
        }else {
            return ResponseEntity.ok(cityService.saveCity(city));
        }
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
    public ResponseEntity<City> updateCityById(@PathVariable Integer id,@RequestBody City city){
        if (id == null || id <=0){
            return ResponseEntity.badRequest().build();
        }

        if (city == null){
            return  ResponseEntity.badRequest().build();
        }
        if (city.getCity() == null || city.getCountry().trim().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if (cityService.getCityById(id).isEmpty()){
            return  ResponseEntity.badRequest().build();
        }

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
