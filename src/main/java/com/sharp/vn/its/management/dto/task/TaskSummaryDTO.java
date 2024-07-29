package com.sharp.vn.its.management.dto.task;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * The type Task summary dto.
 */
@Data
@NoArgsConstructor
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
        this.values = values;
        this.totalCount = totalCount;
    }
}
