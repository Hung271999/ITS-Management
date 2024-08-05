package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.TaskEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The interface Task repository.
 */
@Transactional
public interface TaskRepository extends BaseJpaRepository<TaskEntity, Long>, TaskRepositoryCustom{

    /**
     * Find all page.
     *
     * @param spec the spec
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
     * Find all years list.
     *
     * @return the list
     */
    @Query(value = "SELECT DISTINCT EXTRACT(YEAR FROM t.expiredDate) AS year FROM TaskEntity t")
    List<Integer> findAllYearsFromExpiredDate();

    /**
     * Find distinct weeks from expired date list.
     *
     * @return the list
     */
    @Query("SELECT DISTINCT FUNCTION('date_part', 'week', it.expiredDate) as Week FROM TaskEntity it ORDER BY Week")
    List<Integer> findWeeksFromExpiredDate();
}
