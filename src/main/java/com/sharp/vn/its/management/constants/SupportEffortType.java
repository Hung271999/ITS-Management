package com.sharp.vn.its.management.constants;

import com.sharp.vn.its.management.exception.DataValidationException;

import java.util.Arrays;

/**
 * The enum Support effort type.
 */
public enum SupportEffortType {

    /**
     * Ojt support effort type.
     */
    OJT(1, "OJT"),

    /**
     * Qa support effort type.
     */
    QA(2, "Q&A"),

    /**
     * Smef support effort type.
     */
    SMEF(3, "SMEF"),

    /**
     * Smtl support effort type.
     */
    SMTL(4, "SMTL"),

    /**
     * Ssc support effort type.
     */
    SSC(5, "SSC"),

    /**
     * Support effort type.
     */
    SUPPORT(6, "Support"),

    /**
     * The Trouble shooting.
     */
    TROUBLE_SHOOTING(7, "Trouble Shooting"),

    /**
     * Manual support effort type.
     */
    MANUAL(8, "マニュアル"),

    /**
     * Transfer support effort type.
     */
    TRANSFER(9, "移管"),

    /**
     * Handover support effort type.
     */
    HANDOVER(10, "引継ぎ"),

    /**
     * Monitor support effort type.
     */
    MONITOR(11, "監視"),

    /**
     * Mtg support effort type.
     */
    MTG(12, "MTG");

    /**
     * The Type.
     */
    private final int type;

    /**
     * The Description.
     */
    private final String description;

    /**
     * Instantiates a new Support effort type.
     *
     * @param type        the type
     * @param description the description
     */
    SupportEffortType(int type, String description) {
        this.type = type;
        this.description = description;
    }

    /**
     * Value of support effort type.
     *
     * @param type the type
     * @return the support effort type
     */
    public static SupportEffortType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(taskType -> taskType.getType() == type)
                .findFirst()
                .orElseThrow(() -> new DataValidationException("Invalid support effort type"));
    }

    /**
     * Value of description support effort type.
     *
     * @param description the description
     * @return the support effort type
     */
    public static SupportEffortType valueOfDescription(String description) {
        return Arrays.stream(values())
                .filter(taskStatus -> taskStatus.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new DataValidationException("Invalid support effort description: " + description));
    }

    /**
     * Gets.
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
