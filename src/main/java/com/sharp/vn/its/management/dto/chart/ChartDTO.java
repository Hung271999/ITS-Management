package com.sharp.vn.its.management.dto.chart;

import lombok.Data;

import java.util.List;

/**
 * The type Data dto.
 */
@Data
public class ChartDTO {
    private List<DataItemChart> data;
    private TotalItemChart total;

    private List<Long> userIds;
    private List <Integer> years;
}
