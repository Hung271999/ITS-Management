package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.TaskTypeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Task type repository.
 */
@Transactional
public interface TaskTypeRepository extends BaseJpaRepository<TaskTypeEntity, Long> {
}
