package com.sharp.vn.its.management.entity;

import jakarta.persistence.EmbeddedId;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type User role entity.
 */
@Entity
@Table(name = "its_user_role")
@Data
@NoArgsConstructor
public class UserRoleEntity {
    /**
     * The Id.
     */
    @EmbeddedId
    private UserRoleKey id;

    /**
     * The User.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /**
     * The Role.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    /**
     * Instantiates a new User role entity.
     *
     * @param user the user
     * @param role the role
     */
    public UserRoleEntity(UserEntity user, RoleEntity role) {
        this.user = user;
        this.role = role;
        this.id = new UserRoleKey();
        this.id.setUserId(user.getId());
        this.id.setRoleId(role.getRoleId());
    }
}
