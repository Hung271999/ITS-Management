package com.sharp.vn.its.management.repository;

import com.sharp.vn.its.management.constants.ITSRole;
import com.sharp.vn.its.management.entity.RoleEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleName(ITSRole name);
}
