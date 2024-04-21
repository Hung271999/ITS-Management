package com.sharp.vn.its.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "its_token")
public class TokenEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_Id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "token")
    private String token;

}
