package com.sharp.vn.its.management.dto.task;

import java.util.Map;

import lombok.*;


/**
 * The type Task summary dto.
 */
@Data
public class TaskSummaryDTO {
    private Map<Integer, Number> values;
    private Number totalCount;

    /**
     * Instantiates a new Task summary dto.
     *
     * @param values     the values
     * @param totalCount the total count
     */
    public TaskSummaryDTO(Map<Integer, Number> values, Number totalCount) {
        this.values = values;
        this.totalCount = totalCount;
    }
}
