package com.projectzero.repositories;

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

    @Query("SELECT u FROM User u JOIN FETCH u.cities WHERE u.id = :id")
    Optional<User> findByIdWithCities(@Param("id") Integer id);

    Optional<User> findByLogin(String login);
}
