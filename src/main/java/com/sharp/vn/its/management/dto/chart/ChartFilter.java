package com.sharp.vn.its.management.dto.chart;

import com.sharp.vn.its.management.filter.SortCriteria;
import lombok.*;

import java.util.*;

@Data
public class ChartFilter {
    private List<Long> systemIds;
    private List<Integer> years;
    private Map<String, SortCriteria> sort;
}
