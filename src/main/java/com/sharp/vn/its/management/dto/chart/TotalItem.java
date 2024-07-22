package com.sharp.vn.its.management.dto.chart;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * The type Total item.
 */
@Data

public class TotalItem {
    private List<ChartDTO> data;
    private TotalDTO total;

    /**
     * Instantiates a new Total item.
     *
     * @param data  the data
     * @param total the total
     */
    public TotalItem(List<ChartDTO> data, TotalDTO total) {
        this.data = data;
        this.total = total;
    }
}
