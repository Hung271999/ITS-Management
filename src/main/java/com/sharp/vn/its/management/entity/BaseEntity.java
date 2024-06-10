package com.sharp.vn.its.management.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * The type Base entity.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    /**
     * The Created date.
     */
    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;
    /**
     * The Updated date.
     */
    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;
    /**
     * The Created by.
     */
    @Column(name = "created_by")
    private String createdBy;
    /**
     * The Updated by.
     */
    @Column(name = "updated_by")
    private String updatedBy;

}

