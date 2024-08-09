package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.GroupEntity;
import jakarta.transaction.Transactional;

/**
 * The interface Group repository.
 */
@Transactional
public interface GroupRepository extends BaseJpaRepository<GroupEntity, Long>{
}