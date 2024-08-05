package com.projectzero.services;


import com.projectzero.models.City;
import com.projectzero.repositories.CityRepo;
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
        try {
            return  cityRepo.save(city);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public List<City> getAllCities() { return cityRepo.findAll();}

    public Optional<City> getCityById(Integer id){
        return  cityRepo.findById(id);
    }

    public Optional<City> patchCity(Integer id, City city){
        Optional<City> cityfound = cityRepo.findById(id);
        if (!cityfound.isEmpty()) {
            Optional<City> found = Optional.of(cityRepo.save(city));
            return found;
        }

        return cityfound;
    }
    public void deleteCityById(Integer id){
        cityRepo.deleteById(id);
    }


}
