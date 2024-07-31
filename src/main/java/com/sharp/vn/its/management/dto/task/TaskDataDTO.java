package com.sharp.vn.its.management.dto.task;

import lombok.*;

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
     * @param data  the data
     * @param total the total
     */
    public TaskDataDTO(TaskSummaryDTO total,List<TaskDetailDTO> data) {
        this.data = data;
        this.total = total;
    }
}
