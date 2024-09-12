package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.SupportEffortEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The interface Support effort repository.
 */
@Transactional
public interface SupportEffortRepository extends BaseJpaRepository<SupportEffortEntity, Long>{

    /**
     * Find all page.
     *
     * @param spec     the spec
     * @param pageable the pageable
     * @return the page
     */
    Page<SupportEffortEntity> findAll(Specification<SupportEffortEntity> spec, Pageable pageable);

    @Query("SELECT se.type FROM SupportEffortEntity se")
    List<Integer> findAllTypeIds();
}
