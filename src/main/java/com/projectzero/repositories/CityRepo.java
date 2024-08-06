package com.projectzero.repositories;

import com.projectzero.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepo extends JpaRepository<City, Integer> {

    Optional<City> findByCity(String city);
    Optional<City> findByCityAndCountry(String city, String country);
}
