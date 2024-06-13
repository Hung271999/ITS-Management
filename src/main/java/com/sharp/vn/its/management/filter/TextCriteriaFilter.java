/*
 * (C) Copyright Hitachi Vantara Vietnam (HVN) 2021. All rights reserved. Proprietary and confidential.
 */
package com.sharp.vn.its.management.filter;

import com.sharp.vn.its.management.constants.TextFilterType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Text criteria filter.
 */
@Data
@NoArgsConstructor
public class TextCriteriaFilter {

    /**
     * The Value.
     */
    private String value;

    /**
     * The Type.
     */
    private int type;

}
