package com.sharp.vn.its.management.dto;

import com.sharp.vn.its.management.entity.TaskTypeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskTypeDto {
    Long typeId;
    String typeName;

    public TaskTypeDto(TaskTypeEntity taskType) {
        this.typeId = taskType.getId();
        this.typeName = taskType.getName();
    }
}
