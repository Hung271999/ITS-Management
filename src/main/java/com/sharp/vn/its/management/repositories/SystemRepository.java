package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.SystemEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The interface System repository.
 */
@Transactional
public interface SystemRepository extends BaseJpaRepository<SystemEntity, Long> {
    /**
     * Find all page.
     *
     * @param searchParam the searchParam
     * @param pageable the pageable
     * @return the page
     */
    @Query("SELECT s FROM SystemEntity s WHERE :searchParam IS NULL OR s.systemName LIKE %:searchParam%")
    Page<SystemEntity> findAllSystems(@Param("searchParam") String searchParam, Pageable pageable);
}
