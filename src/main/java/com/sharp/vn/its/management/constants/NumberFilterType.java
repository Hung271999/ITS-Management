/*
 * (C) Copyright Hitachi Vantara Vietnam (HVN) 2021. All rights reserved. Proprietary and confidential.
 */
package com.sharp.vn.its.management.constants;

/**
 * The enum Number filter type.
 */
public enum NumberFilterType {

  /**
   * The Is equal to.
   */
  IS_EQUAL_TO((short) 1, "Is equal to"),

  /**
   * The Is not equal to.
   */
  IS_NOT_EQUAL_TO((short) 2, "Is not equal to"),

  /**
   * The Is greater than.
   */
  IS_GREATER_THAN((short) 3, "Is greater than"),

  /**
   * The Is greater or equal to.
   */
  IS_GREATER_OR_EQUAL_TO((short) 4, "Is greater or equal to"),

  /**
   * Is less than number filter type.
   */
  IS_LESS_THAN((short) 5, "Is_less_than"),

  /**
   * The Is less than or equal to.
   */
  IS_LESS_THAN_OR_EQUAL_TO((short) 6, "Is less than or equal to"),

  /**
   * The Is between.
   */
  IS_BETWEEN((short) 7, "Is between"),

  /**
   * The Is not between.
   */
  IS_NOT_BETWEEN((short) 8, "Is not between");

  /**
   * The Value.
   */
  private final short value;

  /**
   * The Text.
   */
  private final String text;

  /**
   * Instantiates a new Number filter type.
   *
   * @param value the value
   * @param status the status
   */
  NumberFilterType(final short value, final String status) {
    this.value = value;
    this.text = status;
  }

  /**
   * From value number filter type.
   *
   * @param value the value
   * @return the number filter type
   */
  public static NumberFilterType fromValue(final int value) {
    for (final NumberFilterType eventStatus : NumberFilterType.values()) {
      if (eventStatus.value == value) {
        return eventStatus;
      }
    }
    return null;
  }

  /**
   * From status number filter type.
   *
   * @param status the status
   * @return the number filter type
   */
  public static NumberFilterType fromStatus(final String status) {
    for (final NumberFilterType eventStatus : NumberFilterType.values()) {
      if (eventStatus.text.equalsIgnoreCase(status)) {
        return eventStatus;
      }
    }
    return null;
  }

  /**
   * Convert to number filter type number filter type.
   *
   * @param value the value
   * @return the number filter type
   */
  public static NumberFilterType convertToNumberFilterType(DateTimeFilterType value) {
    switch (value) {
    case DATE_TIME_IS:
      return NumberFilterType.IS_EQUAL_TO;
    case DATE_TIME_IS_NOT:
      return NumberFilterType.IS_NOT_EQUAL_TO;
    case DATE_TIME_IS_BEFORE:
      return NumberFilterType.IS_LESS_THAN;
    case DATE_TIME_IS_AFTER:
      return NumberFilterType.IS_GREATER_THAN;
    case DATE_TIME_IS_BETWEEN:
      return NumberFilterType.IS_BETWEEN;
    case DATE_TIME_IS_NOT_BETWEEN:
      return NumberFilterType.IS_NOT_BETWEEN;
    default:
      throw new IllegalArgumentException();
    }
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
  public short getValue() {
    return value;
  }
}
