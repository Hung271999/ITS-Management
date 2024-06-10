package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.TaskTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Task type repository.
 */
@Repository
public interface TaskTypeRepository extends JpaRepository<TaskTypeEntity, Long> {
}
