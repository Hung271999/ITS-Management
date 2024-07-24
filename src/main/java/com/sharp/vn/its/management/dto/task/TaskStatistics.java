package com.sharp.vn.its.management.dto.task;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * The type Task statistics.
 */
@Data
@NoArgsConstructor
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
        this.values = values;
        this.totalCount = totalCount;
    }
}
