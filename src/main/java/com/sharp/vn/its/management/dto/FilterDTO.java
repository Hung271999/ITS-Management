package com.sharp.vn.its.management.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * The type Filter dto.
 */
@Getter
@Setter
@NoArgsConstructor
public class FilterDTO<T> {
    /**
     * The Sort.
     */
    private String sort;
    /**
     * The Order.
     */
    private String order;
    /**
     * The Search.
     */
    private T search;

    /**
     * Instantiates a new Filter dto.
     *
     * @param order the order
     * @param sort the sort
     */
    public FilterDTO(String order, String sort) {
        this.order = order;
        this.sort = sort;
    }
}
