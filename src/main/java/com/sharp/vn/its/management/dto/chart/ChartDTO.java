package com.sharp.vn.its.management.dto.chart;
import lombok.*;

import java.util.List;

/**
 * The type Total item.
 */
@Data

public class ChartDTO {
    private List<ChartDataItem> data;
    private ChartTotalItem total;

    /**
     * Instantiates a new Total item.
     *
     * @param data  the data
     * @param total the total
     */
    public ChartDTO(List<ChartDataItem> data, ChartTotalItem total) {
        this.data = data;
        this.total = total;
    }
}
