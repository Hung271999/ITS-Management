package com.sharp.vn.its.management.dto.task;

import lombok.Data;

import java.util.List;

/**
 * The type Task data dto.
 */
@Data
public class TaskDataDTO {
    private List<TaskDetailDTO> data;
    private TaskSummaryDTO total;


    /**
     * Instantiates a new Task data dto.
     *
     * @param total the total
     * @param data  the data
     */
    public TaskDataDTO(TaskSummaryDTO total, List<TaskDetailDTO> data) {
        this.total = total;
        this.data = data;
    }
}
