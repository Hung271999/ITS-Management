package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.TaskStatusEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Task status repository.
 */
@Transactional
public interface TaskStatusRepository extends BaseJpaRepository<TaskStatusEntity, Long> {
}
