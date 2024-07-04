package com.sharp.vn.its.management.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.config.Task;

import java.util.Set;

/**
 * The type System entity.
 */
@Entity
@Getter
@Setter
@Table(name = "its_system")
public class SystemEntity extends BaseEntity{
    /**
     * The Id.
     */
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The System name.
     */
    @Column(name =  "system_name")
    private String systemName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedBy")
    private UserEntity createdBy;

    /** The updated by. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UpdatedBy")
    private UserEntity updatedBy;
}
