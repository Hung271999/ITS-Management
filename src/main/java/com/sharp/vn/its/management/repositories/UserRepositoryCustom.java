package com.sharp.vn.its.management.repositories;

import java.util.List;

/**
 * The interface Task repository custom.
 */
public interface UserRepositoryCustom {
    List<Object[]> buildFilterChartCondition(List<Long> userIds, List<Integer> years);
}
