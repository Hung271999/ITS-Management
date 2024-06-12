package com.sharp.vn.its.management.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Base entity.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
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

}

