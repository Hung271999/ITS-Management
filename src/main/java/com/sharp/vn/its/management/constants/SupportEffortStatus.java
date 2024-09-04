package com.sharp.vn.its.management.constants;

import com.sharp.vn.its.management.exception.DataValidationException;

import java.util.Arrays;

/**
 * The enum Support effort status.
 */
public enum SupportEffortStatus {

    /**
     * Implemented support effort status.
     */
    IMPLEMENTED(1, "実施済み"),

    /**
     * In complete support effort status.
     */
    IN_COMPLETE(2, "未完了");

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
     * @param status      the status
     * @param description the description
     */
    SupportEffortStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }

    /**
     * Value of task status.
     *
     * @param status the status
     * @return the task status
     */
    public static SupportEffortStatus valueOf(Integer status) {
        if (status == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(taskStatus -> taskStatus.getStatus() == status)
                .findFirst()
                .orElseThrow(() -> new DataValidationException("Invalid support effort status"));
    }

    /**
     * Value of description support effort status.
     *
     * @param description the description
     * @return the support effort status
     */
    public static SupportEffortStatus valueOfDescription(String description) {
        return Arrays.stream(values())
                .filter(taskStatus -> taskStatus.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new DataValidationException("Invalid support effort description: " + description));
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
