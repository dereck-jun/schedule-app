package com.example.schedule.user.repository;

import com.example.schedule.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email = :email and u.deletedDateTime is null")
    Optional<User> findByEmail(String email);

    @Query("select count(u) > 0 from User u where u.email = :email")
    Boolean existsByEmail(String email);

    @Query("select count(u) > 0 from User u where u.username = :username")
    Boolean existsByUsername(String username);

    @Query("select u from User u where u.id = :userId and u.deletedDateTime is null")
    Optional<User> findUserById(Long userId);
}
