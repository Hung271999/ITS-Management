package com.sharp.vn.its.management.dto;

import com.sharp.vn.its.management.entity.TaskEntity;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@NotNull
public class TaskDto {
    private Long id;

    private UserDto user;

    private LocalDateTime receiveDate;

    private LocalDateTime expiredDate;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String content;

    private Integer cost;

    private String ticketNumber;

    private String ticketURL;

    private String note;

    private TaskStatusDto status;

    private TaskTypeDto type;

    private SystemDto system;


    public TaskDto() {
    }

    public TaskDto(TaskEntity taskEntity) {
        this.id = taskEntity.getId();
        // Assuming you have a method in TaskEntity to get UserEntity
        this.user = new UserDto(taskEntity.getUser());
        this.receiveDate = taskEntity.getReceiveDate();
        this.expiredDate = taskEntity.getExpiredDate();
        this.startDate = taskEntity.getStartDate();
        this.endDate = taskEntity.getEndDate();
        this.content = taskEntity.getContent();
        this.cost = taskEntity.getCost();
        this.ticketNumber = taskEntity.getTicketNumber();
        this.ticketURL = taskEntity.getTicketURL();
        this.note = taskEntity.getNote();
        this.status = new TaskStatusDto(taskEntity.getStatus());
        this.type = new TaskTypeDto(taskEntity.getType());
        this.system = new SystemDto(taskEntity.getSystem());
    }
}
