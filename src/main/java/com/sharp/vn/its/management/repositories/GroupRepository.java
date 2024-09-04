package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.GroupEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The interface Group repository.
 */
@Transactional
public interface GroupRepository extends BaseJpaRepository<GroupEntity, Long>{
    /**
     * Find by group name contains page.
     *
     * @param searchParam the search param
     * @param pageable    the pageable
     * @return the page
     */
    Page<GroupEntity> findByGroupNameContains(@Param("searchParam") String searchParam, Pageable pageable);
}
