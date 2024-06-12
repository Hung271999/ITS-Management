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

    /**
     * The Condition type.
     */
    private Integer conditionType;


    /**
     * Gets critera item by text.
     *
     * @param listItems the list items
     * @return the critera item by text
     */
    public List<Object> getCriteraItemByText(final List<Object> listItems) {
        TextFilterType textFilterType = TextFilterType.fromValue(conditionType);
        if (textFilterType == null) {
            return new ArrayList<>();
        }
        Stream<Object> streamValues = listItems.stream();
        switch (textFilterType) {
            case TEXT_CONTAINS:
                streamValues = streamValues
                        .filter(item -> StringUtils.contains(item.toString().toLowerCase(), value));
                break;
            case TEXT_DOES_NOT_CONTAINS:
                streamValues = streamValues
                        .filter(item -> !StringUtils.contains(item.toString().toLowerCase(),
                                value));
                break;
            case TEXT_STARTS_WITH:
                streamValues = streamValues
                        .filter(item -> StringUtils.startsWith(item.toString().toLowerCase(),
                                value));
                break;
            case TEXT_ENDS_WITH:
                streamValues = streamValues
                        .filter(item -> StringUtils.endsWith(item.toString().toLowerCase(), value));
                break;
            case TEXT_IS_EXACTLY:
                streamValues = streamValues
                        .filter(item -> StringUtils.equals(item.toString().toLowerCase(), value));
                break;
            case TEXT_IS_NOT:
                streamValues = streamValues
                        .filter(item -> !StringUtils.equals(item.toString().toLowerCase(), value));
                break;
            default:
                return new ArrayList<>();
        }

        return streamValues.collect(Collectors.toList());
    }

    /**
     * Is valid filter boolean.
     *
     * @param <T> the type parameter
     * @param data the data
     * @return the boolean
     */
    public <T> boolean isValidFilter(final T data) {
        if (null == data) {
            return false;
        }
        List<Object> listItems = new ArrayList<>();
        listItems.add(data);
        List<Object> result = getCriteraItemByText(listItems);
        return result != null && !result.isEmpty();
    }

    /**
     * Apply filter list.
     *
     * @param <T> the type parameter
     * @param <R> the type parameter
     * @param dataSource the data source
     * @param mapper the mapper
     * @return the list
     */
    public <T, R> List<T> applyFilter(final List<T> dataSource,
            final Function<? super T, ? extends R> mapper) {
        if (null == dataSource || dataSource.isEmpty()) {
            return Collections.emptyList();
        }
        return dataSource.parallelStream().filter(item -> isValidFilter(mapper.apply(item)))
                .collect(Collectors.toList());
    }


    /**
     * Gets type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(final int type) {
        this.type = type;
    }
}
