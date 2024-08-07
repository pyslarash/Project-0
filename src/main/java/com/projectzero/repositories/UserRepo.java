package com.projectzero.repositories;

import com.projectzero.models.City;
import com.projectzero.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByLogin(String login);
    List<User> findByCitiesContains(City city);
}
