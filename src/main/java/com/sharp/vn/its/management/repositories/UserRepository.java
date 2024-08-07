package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


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
     * Find all page.
     *
     * @param spec     the spec
     * @param pageable the pageable
     * @return the page
     */
    Page<UserEntity> findAll(Specification<UserEntity> spec, Pageable pageable);

    /**
     * Exists by username boolean.
     *
     * @param username the username
     * @param id       the id
     * @return the boolean
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.username = :username AND (:id IS NULL OR u.id <> :id)")
    Boolean existsByUsername(@Param("username") String username, @Param("id") Long id);

    /**
     * Exists by email boolean.
     *
     * @param email the email
     * @param id    the id
     * @return the boolean
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.email = :email AND (:id IS NULL OR u.id <> :id)")
    Boolean existsByEmail(@Param("email") String email, @Param("id") Long id);
}
