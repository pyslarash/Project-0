package com.projectzero.repositories;

import com.projectzero.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CityRepo extends JpaRepository<City, Integer> {

}
