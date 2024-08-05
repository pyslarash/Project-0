package com.projectzero.demo.services;

import com.projectzero.demo.models.City;
import com.projectzero.demo.repositories.CityRepo;

import java.util.List;

public interface CityService {

    //Trivial Services
    public City getCity(int id);
    public List<City> getAllCities();
    public City addCity(City a);
    public City updateCity(City change);



}
