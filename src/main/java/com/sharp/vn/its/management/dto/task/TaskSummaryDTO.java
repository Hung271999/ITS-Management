package com.sharp.vn.its.management.dto.task;

import java.util.Map;

import lombok.*;


/**
 * The type Task summary dto.
 */
@Data
public class TaskSummaryDTO {
    private Map<Integer, Integer> values;
    private int totalCount;

    /**
     * Instantiates a new Task summary dto.
     *
     * @param values     the values
     * @param totalCount the total count
     */
    public TaskSummaryDTO(Map<Integer, Integer> values, int totalCount) {
        this.totalCount = totalCount;
        this.values = values;
    }
}
