package com.sharp.vn.its.management.dto.task;

import lombok.*;

import java.util.List;


/**
 * The type Task data dto.
 */
@Data

public class TaskDataDTO {
    private List<TaskDataItem> data;
    private TaskStatistics total;


    /**
     * Instantiates a new Task data dto.
     *
     * @param data  the data
     * @param total the total
     */
    public TaskDataDTO(List<TaskDataItem> data, TaskStatistics total) {
        this.data = data;
        this.total = total;
    }
}
