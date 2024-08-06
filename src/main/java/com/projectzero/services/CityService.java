package com.projectzero.services;


import com.projectzero.models.City;
import com.projectzero.repositories.CityRepo;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    CityRepo cityRepo;

    @Autowired
    public CityService(CityRepo cityRepo){ this.cityRepo = cityRepo;}

    public City saveCity(City city){
        return  cityRepo.save(city);
    }
    public List<City> getAllCities() { return cityRepo.findAll();}

    public Optional<City> getCityById(Integer id){
        return  cityRepo.findById(id);
    }

    public Optional<City> patchCity(Integer id, City city){
        Optional<City> cityfound = cityRepo.findById(id);
        if (cityfound.isPresent()){
            City exists = cityfound.get();
            exists.setCity(city.getCity());
            City updated = cityRepo.save(exists);

            return Optional.of(updated);
        }
        return Optional.empty();

    }
    public void deleteCityById(Integer id){
        cityRepo.deleteById(id);
    }

    public Optional<City> getCityByCity(String city){
        return cityRepo.findByCity(city);
    }


}
