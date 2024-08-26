package com.sharp.vn.its.management.entity;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type User entity.
 */
@Entity
@Getter
@Setter
@Table(name = "its_user")
public class UserEntity extends BaseEntity {
    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * The First name.
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * The Last name.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * The Full name.
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * The Email.
     */
    @Column(name = "email")
    private String email;

    /**
     * The Username.
     */
    @Column(name = "user_name", unique = true)
    private String username;

    /**
     * The Password.
     */
    @Column(name = "password")
    private String password;

    /**
     * The Roles.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UserRoleEntity> roles = new HashSet<>();

    /**
     * The Created by.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    /**
     * The Updated by.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

    /**
     * The Tasks.
     */
    @OneToMany(mappedBy = "personInCharge")
    private List<TaskEntity> tasks;

    /**
     * The Groups.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UserGroupEntity> userGroups = new HashSet<>();
}
