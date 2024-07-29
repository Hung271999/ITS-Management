package com.sharp.vn.its.management.dto.task;
import com.sharp.vn.its.management.filter.SortCriteria;
import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * The type Task filter.
 */
@Data
public class TaskFilter {
    private List<Long> userIds;
    private List<Long> systemIds;
    private List<Integer> years;
    private List<Integer> weeks;
    private Map<String, SortCriteria> sort;
}
