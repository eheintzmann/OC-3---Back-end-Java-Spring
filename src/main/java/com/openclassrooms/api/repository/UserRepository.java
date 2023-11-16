package com.openclassrooms.api.repository;

import com.openclassrooms.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Find user by his email
     *
     * @param email user email
     * @return Optional<User>
     */
    Optional<User> findByEmail(String email);

    /**
     * Check user existence
     *
     * @param email user email
     * @return boolean
     */
    boolean existsByEmail(String email);

}
