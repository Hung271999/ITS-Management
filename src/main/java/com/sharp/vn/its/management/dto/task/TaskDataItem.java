package com.sharp.vn.its.management.dto.task;


import lombok.Data;

import java.util.Map;

/**
 * The type Task data item.
 */
@Data
public class TaskDataItem {
    private String userName;
    private String fullName;
    private String firstName;
    private int systemId;
    private String systemName;
    private Map<Integer, Integer> values;
    private int totalCount;

    /**
     * Instantiates a new Task data item.
     *
     * @param userName   the user name
     * @param fullName   the full name
     * @param firstName  the first name
     * @param systemId   the system id
     * @param systemName the system name
     * @param values     the values
     * @param totalCount the total count
     */
    public TaskDataItem(String userName, String fullName, String firstName, int systemId, String systemName, Map<Integer, Integer> values, int totalCount) {
        this.userName = userName;
        this.fullName = fullName;
        this.firstName = firstName;
        this.systemId = systemId;
        this.systemName = systemName;
        this.values = values;
        this.totalCount = totalCount;
    }

    /**
     * Instantiates a new Task data item.
     */
    public TaskDataItem() {
    }
}
