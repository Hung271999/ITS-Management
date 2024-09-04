package com.sharp.vn.its.management.repositories;


import com.sharp.vn.its.management.entity.UserGroupEntity;
import com.sharp.vn.its.management.entity.UserGroupKey;
import jakarta.transaction.Transactional;

@Transactional
public interface UserGroupRepository extends BaseJpaRepository<UserGroupEntity, UserGroupKey>{
    boolean existsByGroupId(Long groupId);
}
