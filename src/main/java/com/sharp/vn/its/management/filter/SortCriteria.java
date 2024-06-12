/*
 * (C) Copyright Hitachi Vantara Vietnam (HVN) 2021. All rights reserved. Proprietary and confidential.
 */
package com.sharp.vn.its.management.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Sort criteria.
 */
@Data
@NoArgsConstructor
public class SortCriteria {

    /**
     * The Field name.
     */
    private String fieldName;

    /**
     * The Sort type.
     */
    private String sortType;

}
