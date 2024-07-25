package com.sharp.vn.its.management.dto.task;

import java.util.Map;

import lombok.*;


/**
 * The type Task statistics.
 */
@Data

public class TaskStatistics {
    private Map<Integer, Integer> values;
    private int totalCount;

    /**
     * Instantiates a new Task statistics.
     *
     * @param values     the values
     * @param totalCount the total count
     */
    public TaskStatistics(Map<Integer, Integer> values, int totalCount) {
        this.totalCount = totalCount;
        this.values = values;
    }
}
