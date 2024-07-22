package com.sharp.vn.its.management.dto.chart;

import java.util.Map;

import lombok.*;

/**
 * The type Total dto.
 */
@Data

public class TotalDTO {
    private Map<Integer, Integer> value;
    private int totalCount;

    public TotalDTO(Map<Integer, Integer> value, int totalCount) {
        this.totalCount = totalCount;
        this.value = value;
    }
}
