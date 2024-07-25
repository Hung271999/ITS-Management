package com.sharp.vn.its.management.dto.task;

import com.sharp.vn.its.management.filter.SortCriteria;
import lombok.*;

import java.util.*;

/**
 * The type Task filter.
 */
@Data
public class TaskFilter {
    private List<Long> systemIds;
    private List<Integer> years;
    private Map<String, SortCriteria> sort;
}
