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
     * The Condition type.
     */
    private Integer conditionType;
    /**
     * The From value.
     */
    private Number fromValue;
    /**
     * The To value.
     */
    private Number toValue;

    /**
     * Sets to value.
     *
     * @param toValue the to value
     */
    public void setToValue(final String toValue) {
        this.toValue = null;
        if (StringUtils.isNumeric(toValue)) {
            try {
                if (conditionType == null) {
                    this.toValue = NumberFormat.getInstance().parse(toValue);
                    return;
                }
                NumberFilterType num = NumberFilterType.fromValue(conditionType);
                if (NumberFilterType.IS_BETWEEN == num || NumberFilterType.IS_NOT_BETWEEN == num) {
                    this.toValue = NumberFormat.getInstance().parse(toValue);
                } else {
                    this.toValue = NumberFormat.getInstance().parse(this.fromValue.toString());
                }
            } catch (ParseException e) {
                this.toValue = null; // NOSONAR
            }
        }
    }


}
