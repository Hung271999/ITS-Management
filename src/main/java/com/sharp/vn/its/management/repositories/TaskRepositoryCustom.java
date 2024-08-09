package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.data.TaskData;

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
    List<TaskData> findTaskByPersonInCharge(List<Long> userIds, List<Integer> years);

    /**
     * Find task by system list.
     *
     * @param SystemIds the system ids
     * @param years     the years
     * @return the list
     */
    List<TaskData> findTaskBySystem(List<Long> SystemIds, List<Integer> years);


    /**
     * Find task by person in charge per week list.
     *
     * @param userIds the user ids
     * @param years   the years
     * @param weeks   the weeks
     * @return the list
     */
    List<TaskData> findTaskByPersonInChargePerWeek(List<Long> userIds, List<Integer> years, List<Integer> weeks);
}
