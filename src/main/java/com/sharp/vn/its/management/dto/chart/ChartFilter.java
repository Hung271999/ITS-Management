package com.sharp.vn.its.management.dto.chart;
import com.sharp.vn.its.management.filter.SortCriteria;
import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * The type Chart filter.
 */
@Data
public class ChartFilter {
    private List<Long> userIds;
    private List<Integer> years;
    private Map<String, SortCriteria> sort;
}
