package com.sharp.vn.its.management.dto.chart;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * The type Total item.
 */
@Data
@NoArgsConstructor
public class TotalItemChart {
    private Map<Integer, Integer> values;
    private int totalCount;

    public TotalItemChart(Map<Integer, Integer> values, int totalCount) {
        this.values = values;
        this.totalCount = totalCount;
    }
}
