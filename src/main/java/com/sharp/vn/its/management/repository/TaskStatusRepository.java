package com.sharp.vn.its.management.repository;

import com.sharp.vn.its.management.entity.RoleEntity;
import com.sharp.vn.its.management.entity.TaskStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatusEntity, Long> {
}
