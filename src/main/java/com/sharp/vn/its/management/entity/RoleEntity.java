package com.sharp.vn.its.management.entity;


import com.sharp.vn.its.management.constants.ITSRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "its_role")
@Getter
@Setter
public class RoleEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, name="role_name")
    private ITSRole roleName;

    @Column(name = "description")
    private String description;
}

