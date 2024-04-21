package com.sharp.vn.its.management.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.config.Task;

import java.util.Set;
@Entity(name = "its_task_status")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TaskStatusEntity extends BaseEntity{

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name =  "status_name")
    private String name;
}
