package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.TaskStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Task status repository.
 */
@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatusEntity, Long> {
}
