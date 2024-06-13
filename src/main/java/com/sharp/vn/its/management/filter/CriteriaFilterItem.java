/*
 * (C) Copyright Hitachi Vantara Vietnam (HVN) 2021. All rights reserved. Proprietary and confidential.
 */
package com.sharp.vn.its.management.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Criteria filter item.
 */
@Data
@NoArgsConstructor
public class CriteriaFilterItem {

    /**
     * The Field name.
     */
    private String fieldName;

    /**
     * The Type.
     */
    private int type;

    /**
     * The Filter boolean value.
     */
    private BooleanCriteriaFilter filterBooleanValue;
    /**
     * The Filter datetime value.
     */
    private DateTimeCriteriaFilter filterDatetimeValue;

    /**
     * The Filter number value.
     */
    private NumberCriteriaFilter filterNumberValue;

    /**
     * The Filter text value.
     */
    private TextCriteriaFilter filterTextValue;

    /**
     * The Sort criteria.
     */
    private SortCriteria sortCriteria;



}
