package com.sharp.vn.its.management.dto.chart;

import lombok.Data;

import java.util.List;

/**
 * The type Data dto.
 */
@Data
public class ChartDTO {
    private List<ChartIDataItem> data;
    private ChartTotalItem total;

    /**
     * Instantiates a new Chart dto.
     *
     * @param total the total
     * @param data  the data
     */
    public ChartDTO(ChartTotalItem total, List<ChartIDataItem> data) {
        this.total = total;
        this.data = data;
    }
}
