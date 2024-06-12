package com.sharp.vn.its.management.entity;


import com.sharp.vn.its.management.constants.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Role entity.
 */
@Entity(name = "its_role")
@Getter
@Setter
public class RoleEntity {
    /**
     * The Role id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    /**
     * The Role name.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "role_name")
    private Role roleName;

    /**
     * The Description.
     */
    @Column(name = "description")
    private String description;
}

