package com.sharp.vn.its.management.dto.chart;

import lombok.*;


import java.util.Map;

/**
 * The type Chart dto.
 */
@Data
public class ChartDataItem {
    private Long systemId;
    private String systemName;
    private Map<Integer, Integer> values;
    private int totalCount;


    /**
     * Instantiates a new Chart dto.
     *
     * @param systemId   the system id
     * @param systemName the system name
     * @param totalCount the total count
     * @param values      the value
     */
    public ChartDataItem(Long systemId, String systemName, int totalCount, Map<Integer, Integer> values) {
        this.systemId = systemId;
        this.systemName = systemName;
        this.totalCount = totalCount;
        this.values = values;
    }

    /**
     * Instantiates a new Chart dto.
     */
    public ChartDataItem()
    {}
}





