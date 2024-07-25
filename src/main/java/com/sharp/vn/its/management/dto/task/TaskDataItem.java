package com.sharp.vn.its.management.dto.task;

import lombok.*;


import java.util.Map;


/**
 * The type Task data item.
 */
@Data
public class TaskDataItem {
    private Long systemId;
    private String systemName;
    private Map<Integer, Integer> values;
    private int totalCount;


    /**
     * Instantiates a new Task data item.
     *
     * @param systemId   the system id
     * @param systemName the system name
     * @param totalCount the total count
     * @param values     the values
     */
    public TaskDataItem(Long systemId, String systemName, int totalCount, Map<Integer, Integer> values) {
        this.systemId = systemId;
        this.systemName = systemName;
        this.totalCount = totalCount;
        this.values = values;
    }


    /**
     * Instantiates a new Task data item.
     */
    public TaskDataItem() {
    }
}





