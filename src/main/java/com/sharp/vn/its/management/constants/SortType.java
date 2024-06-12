/*
 * (C) Copyright Hitachi Vantara Vietnam (HVN) 2021. All rights reserved. Proprietary and confidential.
 */
package com.sharp.vn.its.management.constants;

/**
 * The Enum SortType.
 */
public enum SortType {

  /** The enabled. */
  ASC(false, "ASC"),

  /** The disabled. */
  DESC(true, "DESC");
  /**
   * The value.
   */
  private final boolean value;

  /** The text. */
  private final String text;

  /**
   * Instantiates a new warm up schedule status.
   *
   * @param value
   *          the value
   * @param sortText
   *          the sort text
   */
  SortType(final boolean value, final String sortText) {
    this.value = value;
    text = sortText;
  }

  /**
   * From value.
   *
   * @param value the value
   * @return the warm up schedule status
   */
  public static SortType fromValue(final boolean value) {
    for (final SortType val : SortType.values()) {
      if (val.value == value) {
        return val;
      }
    }
    return null;
  }
  
  /**
   * From status.
   *
   * @param sortVal
   *          the sort Val
   * @return the sort type
   */
  public static SortType fromText(final String sortVal) {
    for (final SortType sortT : SortType.values()) {
      if (sortT.text.equalsIgnoreCase(sortVal)) {
        return sortT;
      }
    }
    return null;
  }

  /**
   * Gets the text sort.
   *
   * @return the text sort
   */
  public String getText() {
    return text;
  }

  /**
   * Gets the value.
   *
   * @return the value
   */
  public boolean getValue() {
    return value;
  }
}
