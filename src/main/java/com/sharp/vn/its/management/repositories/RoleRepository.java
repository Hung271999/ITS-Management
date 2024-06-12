package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.constants.Role;
import com.sharp.vn.its.management.entity.RoleEntity;
import jakarta.transaction.Transactional;

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
    Optional<RoleEntity> findByRoleName(Role name);
}
