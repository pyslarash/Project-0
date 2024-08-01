package com.projectzero.repositories;

import com.projectzero.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> { // Change Integer to Long
    User findByLogin(String login);
}
