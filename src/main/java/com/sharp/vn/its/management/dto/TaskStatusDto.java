package com.sharp.vn.its.management.dto;

import com.sharp.vn.its.management.entity.TaskEntity;
import com.sharp.vn.its.management.entity.TaskStatusEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class TaskStatusDto {

    Long statusId;

    String statusName;

    public TaskStatusDto(TaskStatusEntity taskStatus) {
        this.statusId = taskStatus.getId();
        this.statusName = taskStatus.getName();
    }

    public TaskStatusDto() {
    }

}
