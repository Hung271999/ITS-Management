package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.TaskEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;

/**
 * The interface Task repository.
 */
@Transactional
public interface TaskRepository extends BaseJpaRepository<TaskEntity, Long> {

    /**
     * Find all page.
     *
     * @param spec     the spec
     * @param pageable the pageable
     * @return the page
     */
    Page<TaskEntity> findAll(Specification<TaskEntity> spec, Pageable pageable);


    /**
     * Exists tasks by user id boolean.
     *
     * @param userId the user id
     * @return the boolean
     */
    Boolean existsByPersonInChargeId(Long userId);

}
