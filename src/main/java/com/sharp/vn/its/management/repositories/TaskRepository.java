package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.TaskEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
     * Exists by system id boolean.
     *
     * @param systemId the system id
     * @return the boolean
     */
    boolean existsBySystemId(Long systemId);


    /**
     * Exists tasks by user id boolean.
     *
     * @param userId the user id
     * @return the boolean
     */
    Boolean existsByPersonInChargeId(Long userId);

    /**
     * Count tasks by status and system list.
     *
     * @return the list
     */
    @Query(value = "SELECT s.system_name, it.status_id, COUNT(it.status_id) AS statusCount " +
            "FROM its_user iu " +
            "JOIN its_task it ON iu.id = it.user_id " +
            "JOIN its_system s ON it.system_id = s.id " +
            "WHERE s.id IN (:systemIds) " +
            "AND EXTRACT(YEAR FROM it.created_date) = :years " +
            "GROUP BY s.system_name, it.status_id", nativeQuery = true)
    List<Object[]> countTasksBySystemAndStatusAndYear(@Param("systemIds") List<Long> systemIds, @Param("years") List<Integer> years);

    @Query(value="select id from its_system", nativeQuery = true)
    List<Long> findAllSystemId();


}
