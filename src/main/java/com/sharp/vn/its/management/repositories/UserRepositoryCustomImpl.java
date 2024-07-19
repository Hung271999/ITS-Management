package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.entity.TaskEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Task repository custom.
 */
@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> buildFilterChartCondition(List<Long> userIds, List<Integer> years) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        // Root entities
        Root<UserEntity> userRoot = cq.from(UserEntity.class);
        Join<UserEntity, TaskEntity> taskJoin = userRoot.join("tasks");

        // Select clause
        cq.multiselect(
                userRoot.get("firstName"),
                taskJoin.get("status"),
                cb.count(taskJoin.get("status"))
        );

        // Where clause
        List<Predicate> predicates = new ArrayList<>();

        if (!userIds.isEmpty()) {
            predicates.add(userRoot.get("id").in(userIds));
        }

        if (!years.isEmpty()) {
            Integer startYear = years.get(0);
            Integer endYear = years.get(years.size() - 1);

            LocalDate startDate = LocalDate.of(startYear, 1, 1);
            LocalDate endDate = LocalDate.of(endYear + 1, 1, 1);

            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atStartOfDay();

            Predicate datePredicate = cb.between(
                    taskJoin.get("createdDate").as(LocalDateTime.class),
                    startDateTime,
                    endDateTime.minusNanos(1)
            );
            predicates.add(datePredicate);
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        // Group by clause
        cq.groupBy(userRoot.get("firstName"), taskJoin.get("status"));

        // Create query
        TypedQuery<Object[]> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
