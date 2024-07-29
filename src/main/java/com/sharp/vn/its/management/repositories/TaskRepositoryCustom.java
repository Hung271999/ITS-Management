package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.data.ChartData;

import java.util.List;

/**
 * The interface Task repository custom.
 */
public interface TaskRepositoryCustom {
    /**
     * Find task by person in charge list.
     *
     * @param userIds the user ids
     * @param years   the years
     * @return the list
     */
    List<ChartData> findTaskByPersonInCharge(List<Long> userIds, List<Integer> years);

    /**
     * Find task by system list.
     *
     * @param SystemIds the system ids
     * @param years     the years
     * @return the list
     */
    List<ChartData> findTaskBySystem(List<Long> SystemIds, List<Integer> years);
}
