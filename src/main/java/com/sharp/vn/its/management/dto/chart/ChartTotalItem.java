package com.sharp.vn.its.management.dto.chart;

import java.util.Map;

import lombok.*;

/**
 * The type Total dto.
 */
@Data

public class ChartTotalItem {
    private Map<Integer, Integer> values;
    private int totalCount;

    public ChartTotalItem(Map<Integer, Integer> values, int totalCount) {
        this.totalCount = totalCount;
        this.values = values;
    }
}
