package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.SystemEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 * The interface System repository.
 */
@Transactional
public interface SystemRepository extends BaseJpaRepository<SystemEntity, Long> {


    /**
     * Find by system name contains page.
     *
     * @param searchParam the search param
     * @param pageable    the pageable
     * @return the page
     */
    Page<SystemEntity> findBySystemNameContains(@Param("searchParam") String searchParam, Pageable pageable);
}


