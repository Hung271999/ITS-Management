package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.constants.ITSRole;
import com.sharp.vn.its.management.entity.RoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Role repository.
 */
@Transactional
public interface RoleRepository extends BaseJpaRepository<RoleEntity, Long> {

    /**
     * Find by role name optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<RoleEntity> findByRoleName(ITSRole name);
}
