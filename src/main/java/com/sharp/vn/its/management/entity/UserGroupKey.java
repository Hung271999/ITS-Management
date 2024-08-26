package com.sharp.vn.its.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

/**
 * The type User role key.
 */
@Embeddable
@Data
public class UserGroupKey implements Serializable {
    /**
     * The User id.
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * The Role id.
     */
    @Column(name = "group_id")
    private Long groupId;
}