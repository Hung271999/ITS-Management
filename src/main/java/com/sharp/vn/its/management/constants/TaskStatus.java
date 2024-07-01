package com.sharp.vn.its.management.constants;

import com.sharp.vn.its.management.exception.DataValidationException;

import java.util.Arrays;

/**
 * The enum Task status.
 */
public enum TaskStatus {
    /**
     * Not start task.
     */
    NOT_START(1, "未着手"),
    /**
     * Complete task status.
     */
    COMPLETE(2, "完了"),
    /**
     * In progress task status.
     */
    IN_PROGRESS(3, "対応中"),
    /**
     * On hold task status.
     */
    ON_HOLD(4, "保留"),
    /**
     * Suspended task status.
     */
    SUSPENDED(5, "中止");

    /**
     * The status.
     */
    private final int status;
    /**
     * The Description.
     */
    private final String description;

    /**
     * Instantiates a new Task status.
     *
     * @param status the status
     * @param description the description
     */
    TaskStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }

    /**
     * Value of task status.
     *
     * @param status the status
     * @return the task status
     */
    public static TaskStatus valueOf(Integer status) {
        if (status == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(taskStatus -> taskStatus.getStatus() == status)
                .findFirst()
                .orElseThrow(() -> new DataValidationException("Invalid task status"));
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
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
