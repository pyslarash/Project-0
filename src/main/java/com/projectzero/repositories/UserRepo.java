package com.projectzero.repositories;

import com.projectzero.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> { // Change Integer to Long

    List<User> findUsersById(Long id);

}
