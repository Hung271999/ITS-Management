package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Task repository.
 */
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
