package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.data.ChartData;

import java.util.List;

/**
 * The interface Task repository custom.
 */
public interface UserRepositoryCustom {
    List<ChartData> findUserGroupByNameAndStatus(List<Long> userIds, List<Integer> years);
}
