package com.sharp.vn.its.management.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharp.vn.its.management.entity.TaskStatusEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Task status dto.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskStatusDTO {

    /**
     * The Status id.
     */
    private Long statusId;

    /**
     * The Status name.
     */
    private String statusName;

    /**
     * Instantiates a new Task status dto.
     *
     * @param taskStatus the task status
     */
    public TaskStatusDTO(TaskStatusEntity taskStatus) {
        this.statusId = taskStatus.getId();
        this.statusName = taskStatus.getTaskStatus();
    }

}
