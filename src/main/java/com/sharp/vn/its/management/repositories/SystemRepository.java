package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.SystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface System repository.
 */
@Repository
public interface SystemRepository extends JpaRepository<SystemEntity, Long> {
}
