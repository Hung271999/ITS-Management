package com.sharp.vn.its.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type User role entity.
 */
@Entity
@Table(name = "its_user_group")
@Data
@NoArgsConstructor
public class UserGroupEntity {
    /**
     * The Id.
     */
    @EmbeddedId
    private UserGroupKey id;

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
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    /**
     * Instantiates a new User group entity.
     *
     * @param user the user
     * @param group the group
     */
    public UserGroupEntity(UserEntity user, GroupEntity group) {
        this.user = user;
        this.group = group;
        this.id = new UserGroupKey();
        this.id.setUserId(user.getId());
        this.id.setGroupId(group.getId());
    }
}
