/*
 * (C) Copyright Hitachi Vantara Vietnam (HVN) 2021. All rights reserved. Proprietary and confidential.
 */
package com.sharp.vn.its.management.constants;

/**
 * The enum Date time filter type.
 */
public enum DateTimeFilterType {

    /**
     * The Date time is.
     */
    DATE_TIME_IS((short) 1, "Date time is"),

    /**
     * The Date time is not.
     */
    DATE_TIME_IS_NOT((short) 2, "Date time is not"),

    /**
     * The Date time is before.
     */
    DATE_TIME_IS_BEFORE((short) 3, "Date time is before"),

    /**
     * The Date time is after.
     */
    DATE_TIME_IS_AFTER((short) 4, "Date time is after"),

    /**
     * The Date time is between.
     */
    DATE_TIME_IS_BETWEEN((short) 5, "Date time is between"),

    /**
     * The Date time is not between.
     */
    DATE_TIME_IS_NOT_BETWEEN((short) 6, "Date time is not between");

    /**
     * The Value.
     */
    private final Short value;

    /**
     * The Text.
     */
    private final String text;

    /**
     * Instantiates a new Date time filter type.
     *
     * @param value the value
     * @param status the status
     */
    DateTimeFilterType(final Short value, final String status) {
        this.value = value;
        this.text = status;
    }

    /**
     * From value date time filter type.
     *
     * @param value the value
     * @return the date time filter type
     */
    public static DateTimeFilterType fromValue(final int value) {
        for (final DateTimeFilterType eventStatus : DateTimeFilterType.values()) {
            if (eventStatus.value == value) {
                return eventStatus;
            }
        }
        return null;
    }

    /**
     * From status date time filter type.
     *
     * @param status the status
     * @return the date time filter type
     */
    public static DateTimeFilterType fromStatus(final String status) {
        for (final DateTimeFilterType eventStatus : DateTimeFilterType.values()) {
            if (eventStatus.text.equalsIgnoreCase(status)) {
                return eventStatus;
            }
        }
        return null;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Short getValue() {
        return value;
    }
}
