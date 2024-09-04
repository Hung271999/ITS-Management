package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.SupportEffortEntity;
import jakarta.transaction.Transactional;

@Transactional
public interface SupportEffortRepository extends BaseJpaRepository<SupportEffortEntity, Long>{
}
