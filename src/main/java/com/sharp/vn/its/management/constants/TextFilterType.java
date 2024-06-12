/*
 * (C) Copyright Hitachi Vantara Vietnam (HVN) 2021. All rights reserved. Proprietary and confidential.
 */
package com.sharp.vn.its.management.constants;

/**
 * The enum Text filter type.
 */
public enum TextFilterType {

    /**
     * The Text contains.
     */
    TEXT_CONTAINS(1, "Text contains"),

    /**
     * The Text does not contains.
     */
    TEXT_DOES_NOT_CONTAINS(2, "Text does not contain"),

    /**
     * The Text starts with.
     */
    TEXT_STARTS_WITH(3, "Text starts with"),

    /**
     * The Text ends with.
     */
    TEXT_ENDS_WITH(4, "Text ends with"),

    /**
     * The Text is exactly.
     */
    TEXT_IS_EXACTLY(5, "Text is exactly"),

    /**
     * The Text is not.
     */
    TEXT_IS_NOT(6, "Text is not");

    /**
     * The Value.
     */
    private final int value;

    /**
     * The Text.
     */
    private final String text;

    /**
     * Instantiates a new Text filter type.
     *
     * @param value the value
     * @param text the text
     */
    TextFilterType(final int value, final String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * From value text filter type.
     *
     * @param value the value
     * @return the text filter type
     */
    public static TextFilterType fromValue(final int value) {
        for (final TextFilterType e : TextFilterType.values()) {
            if (e.value == value) {
                return e;
            }
        }
        return null;
    }

    /**
     * From value text filter type.
     *
     * @param value the value
     * @return the text filter type
     */
    public static TextFilterType fromValue(final String value) {
        for (final TextFilterType e : TextFilterType.values()) {
            if (e.text.equalsIgnoreCase(value)) {
                return e;
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
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return text;
    }
}
