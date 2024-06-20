package com.sharp.vn.its.management.constants;

import com.sharp.vn.its.management.exception.DataValidationException;

import java.util.Arrays;

/**
 * The enum Task type.
 */
public enum TaskType {

    /**
     * Any time task type.
     */
    ANY_TIME(1, "随時"),
    /**
     * Monthly task type.
     */
    MONTHLY(2, "月次"),
    /**
     * Weekly task type.
     */
    WEEKLY(3, "週次"),
    /**
     * Daily task type.
     */
    DAILY(4, "日次"),
    /**
     * Yearly task type.
     */
    YEARLY(5, "年次");

    /**
     * The Type.
     */
    private final int type;
    /**
     * The Description.
     */
    private final String description;

    /**
     * Instantiates a new Task type.
     *
     * @param type the type
     * @param description the description
     */
    TaskType(int type, String description) {
        this.type = type;
        this.description = description;
    }

    /**
     * Value of task type.
     *
     * @param type the type
     * @return the task type
     */
    public static TaskType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(taskType -> taskType.getType() == type)
                .findFirst()
                .orElseThrow(() -> new DataValidationException("Invalid task type"));
    }

    /**
     * Gets .
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

}
