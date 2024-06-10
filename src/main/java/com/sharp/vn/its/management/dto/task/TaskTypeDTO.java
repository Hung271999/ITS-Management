package com.sharp.vn.its.management.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharp.vn.its.management.dto.BaseDTO;
import com.sharp.vn.its.management.entity.TaskTypeEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Task type dto.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskTypeDTO extends BaseDTO {
    /**
     * The Type id.
     */
    private Long typeId;
    /**
     * The Type name.
     */
    private String typeName;

    /**
     * Instantiates a new Task type dto.
     *
     * @param taskType the task type
     */
    public TaskTypeDTO(TaskTypeEntity taskType) {
        this.typeId = taskType.getId();
        this.typeName = taskType.getTaskType();
    }
}
