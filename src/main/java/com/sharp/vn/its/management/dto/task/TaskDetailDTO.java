package com.sharp.vn.its.management.dto.task;

import lombok.*;

import java.util.Map;

/**
 * The type Task data item.
 */
@Data
public class TaskDetailDTO {
    private Long systemId;
    private String systemName;
    private String week;
    private Map<Integer, Integer> values;
    private int totalCount;


    /**
     * Instantiates a new Task detail dto.
     *
     * @param systemId   the system id
     * @param systemName the system name
     * @param totalCount the total count
     * @param values     the values
     */
    public TaskDetailDTO(Long systemId, String systemName, int totalCount, Map<Integer, Integer> values) {
        this.systemId = systemId;
        this.systemName = systemName;
        this.totalCount = totalCount;
        this.values = values;
    }

    /**
     * Instantiates a new Task detail dto.
     */
    public TaskDetailDTO() {
    }
}





