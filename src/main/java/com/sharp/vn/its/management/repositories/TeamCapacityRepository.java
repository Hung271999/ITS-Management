package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.SystemEntity;
import com.sharp.vn.its.management.entity.TeamCapacityEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 * The interface Team capacity repository.
 */
@Transactional
public interface TeamCapacityRepository extends BaseJpaRepository<TeamCapacityEntity, Long> {

}
