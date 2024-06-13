/*
 * (C) Copyright Hitachi Vantara Vietnam (HVN) 2021. All rights reserved. Proprietary and confidential.
 */
package com.sharp.vn.its.management.filter;

import com.sharp.vn.its.management.constants.NumberFilterType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * The type Number criteria filter.
 */
@Data
@NoArgsConstructor
public class NumberCriteriaFilter {

    /**
     * The From value.
     */
    private Number fromValue;
    /**
     * The To value.
     */
    private Number toValue;

}
