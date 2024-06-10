package com.sharp.vn.its.management.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.config.Task;

import java.util.Set;

/**
 * The type Task type entity.
 */
@Entity(name = "its_task_type")
@Getter
@Setter
public class TaskTypeEntity extends BaseEntity {
    /**
     * The Id.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The Task type.
     */
    @Column(name = "task_type")
    private String taskType;
}
