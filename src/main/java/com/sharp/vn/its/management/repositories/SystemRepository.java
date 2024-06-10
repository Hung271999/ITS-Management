package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.SystemEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface System repository.
 */
@Transactional
public interface SystemRepository extends BaseJpaRepository<SystemEntity, Long> {
}
