package com.sharp.vn.its.management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

/**
 * The type User role key.
 */
@Embeddable
@Data
public class UserRoleKey implements Serializable {
    /**
     * The User id.
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * The Role id.
     */
    @Column(name = "role_id")
    private Long roleId;
}
