package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.data.ChartData;

import java.util.List;

/**
 * The interface Task repository custom.
 */
public interface TaskRepositoryCustom {
    List<ChartData> findTaskByPersonInCharge(List<Long> userIds, List<Integer> years);
}
