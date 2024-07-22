package com.sharp.vn.its.management.dto.chart;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * The type Total item.
 */
@Data
@NoArgsConstructor
public class ChartTotalItem {
    private Map<Integer, Integer> values;
    private int totalCount;

    /**
     * Instantiates a new Chart item total.
     *
     * @param values     the values
     * @param totalCount the total count
     */
    public ChartTotalItem(Map<Integer, Integer> values, int totalCount) {
        this.values = values;
        this.totalCount = totalCount;
    }
}
