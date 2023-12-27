package com.example.emailsender.repository;

import com.example.emailsender.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);
    Boolean existsByEmail(String email);
}
