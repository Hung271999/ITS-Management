package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.data.ChartData;
import com.sharp.vn.its.management.entity.SystemEntity;
import com.sharp.vn.its.management.entity.TaskEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Task repository custom.
 */
@Repository
public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ChartData> findTaskByPersonInCharge(List<Long> userIds, List<Integer> years) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<UserEntity> userRoot = cq.from(UserEntity.class);
        Join<UserEntity, TaskEntity> taskJoin = userRoot.join("tasks");

        cq.multiselect(
                userRoot.get("id"),
                userRoot.get("firstName"),
                taskJoin.get("status"),
                cb.count(taskJoin.get("status"))
        );

        List<Predicate> predicates = new ArrayList<>();
        if (!userIds.isEmpty()) {
            predicates.add(userRoot.get("id").in(userIds));
        }
        if (!years.isEmpty()) {
            predicates.add(createYearPredicateForUserJoin(cb, taskJoin, years));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.groupBy(userRoot.get("firstName"), taskJoin.get("status"), userRoot.get("id"));
        TypedQuery<Object[]> query = entityManager.createQuery(cq);
        return query.getResultList().stream().map(row -> {
            ChartData chartData = new ChartData();
            chartData.setId(((Number) row[0]).longValue());
            chartData.setFirstName((String) row[1]);
            chartData.setStatus(((Number) row[2]).intValue());
            chartData.setTotal(((Number) row[3]).intValue());
            return chartData;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ChartData> findTaskBySystem(List<Long> systemIds, List<Integer> years) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<SystemEntity> systemRoot = cq.from(SystemEntity.class);
        Join<SystemEntity, TaskEntity> taskJoin = systemRoot.join("tasks");

        cq.multiselect(
                systemRoot.get("id"),
                systemRoot.get("systemName"),
                taskJoin.get("status"),
                cb.count(taskJoin.get("status"))
        );

        List<Predicate> predicates = new ArrayList<>();
        if (!systemIds.isEmpty()) {
            predicates.add(systemRoot.get("id").in(systemIds));
        }
        if (!years.isEmpty()) {
            predicates.add(createYearPredicateForSystemJoin(cb, taskJoin, years));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.groupBy(systemRoot.get("systemName"), taskJoin.get("status"), systemRoot.get("id"));

        TypedQuery<Object[]> query = entityManager.createQuery(cq);
        return query.getResultList().stream().map(row -> {
            ChartData chartData = new ChartData();
            chartData.setId(((Number) row[0]).longValue());
            chartData.setSystemName((String) row[1]);
            chartData.setStatus(((Number) row[2]).intValue());
            chartData.setTotal(((Number) row[3]).intValue());
            return chartData;
        }).collect(Collectors.toList());
    }
    
    private Predicate createYearPredicateForUserJoin(CriteriaBuilder cb, Join<UserEntity, TaskEntity> taskJoin, List<Integer> years) {
        List<Predicate> yearPredicates = new ArrayList<>();
        years.forEach(t -> {
            final Year year = Year.of(t);
            LocalDateTime startDateTime = year.atDay(1).atStartOfDay();
            LocalDateTime endDateTime = year.atDay(year.length()).atStartOfDay();
            Predicate datePredicate = cb.between(
                    taskJoin.get("expiredDate").as(LocalDateTime.class),
                    startDateTime,
                    endDateTime
            );
            yearPredicates.add(datePredicate);
        });
        return cb.or(yearPredicates.toArray(new Predicate[0]));
    }

    private Predicate createYearPredicateForSystemJoin(CriteriaBuilder cb, Join<SystemEntity, TaskEntity> taskJoin, List<Integer> years) {
        List<Predicate> yearPredicates = new ArrayList<>();
        years.forEach(t -> {
            final Year year = Year.of(t);
            LocalDateTime startDateTime = year.atDay(1).atStartOfDay();
            LocalDateTime endDateTime = year.atDay(year.length()).atStartOfDay();
            Predicate datePredicate = cb.between(
                    taskJoin.get("expiredDate").as(LocalDateTime.class),
                    startDateTime,
                    endDateTime
            );
            yearPredicates.add(datePredicate);
        });
        return cb.or(yearPredicates.toArray(new Predicate[0]));
    }
}
