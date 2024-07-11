package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface User repository.
 */
@Transactional
public interface UserRepository extends BaseJpaRepository<UserEntity, Long> {

    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Exists by username boolean.
     *
     * @param username the username
     * @return the boolean
     */
    Boolean existsByUsername(String username);

    /**
     * Exists by email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    Boolean existsByEmail(String email);

    /**
     * Find user by first name.
     *
     * @param firstName the firstName
     * @return user entity
     */
    UserEntity findByFirstName(String firstName);
}
