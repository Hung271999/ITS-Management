/*
 * (C) Copyright Hitachi Vantara Vietnam (HVN) 2021. All rights reserved. Proprietary and confidential.
 */
package com.sharp.vn.its.management.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Boolean criteria filter.
 */
@Data
@NoArgsConstructor
public class BooleanCriteriaFilter {

  /**
   * The Id.
   */
  private Object id;

  /**
   * The Value.
   */
  private Object value;
}
