package com.sharp.vn.its.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Group entity.
 */
@Entity
@Getter
@Setter
@Table(name = "its_group")
public class GroupEntity extends BaseEntity {
    /**
     * The Id.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The Group name.
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * The Description.
     */
    @Column(name = "description")
    private String description;

    /**
     * The Created by.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedBy") 
    private UserEntity createdBy;

    /**
     * The Updated by.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UpdatedBy")
    private UserEntity updatedBy;

    /**
     * The User groups.
     */
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserGroupEntity> userGroups  = new HashSet<>();
}
