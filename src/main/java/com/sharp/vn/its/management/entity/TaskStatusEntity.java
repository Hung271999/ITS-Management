package com.sharp.vn.its.management.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.config.Task;

import java.util.Set;

/**
 * The type Task status entity.
 */
@Entity(name = "its_task_status")
@Getter
@Setter
public class TaskStatusEntity extends BaseEntity {

    /**
     * The Id.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The Task status.
     */
    @Column(name = "task_status")
    private String taskStatus;
}
