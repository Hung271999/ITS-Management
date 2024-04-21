package com.sharp.vn.its.management.repository;

import com.sharp.vn.its.management.entity.TaskEntity;
import com.sharp.vn.its.management.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByUserId(Long userId);
}
