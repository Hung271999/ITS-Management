/*
 * (C) Copyright Hitachi Vantara Vietnam (HVN) 2021. All rights reserved. Proprietary and confidential.
 */
package com.sharp.vn.its.management.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * The type Date time criteria filter.
 */
@Data
@NoArgsConstructor
public class DateTimeCriteriaFilter {
  /**
   * The Date.
   */
  private Instant date;

  /**
   * The Hour.
   */
  private int hour;

  /**
   * The Minute.
   */
  private int minute;

  /**
   * The Second.
   */
  private int second;

  /**
   * The Milisecond.
   */
  private int milisecond;

  /**
   * The Start time.
   */
  private Long startTime;

  /**
   * The End time.
   */
  private Long endTime;

}
