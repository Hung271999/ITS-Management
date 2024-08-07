package com.sharp.vn.its.management.repositories;

import com.sharp.vn.its.management.data.TaskData;
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
    public List<TaskData> findTaskByPersonInCharge(List<Long> userIds, List<Integer> years) {
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
            predicates.add(createYearPredicate(cb, taskJoin, years));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.groupBy(userRoot.get("firstName"), taskJoin.get("status"), userRoot.get("id"));
        TypedQuery<Object[]> query = entityManager.createQuery(cq);
        return query.getResultList().stream().map(row -> {
            TaskData taskData = new TaskData();
            taskData.setId(((Number) row[0]).longValue());
            taskData.setFirstName((String) row[1]);
            taskData.setStatus(((Number) row[2]).intValue());
            taskData.setTotal(((Number) row[3]).intValue());
            return taskData;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TaskData> findTaskBySystem(List<Long> systemIds, List<Integer> years) {
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
            predicates.add(createYearPredicate(cb, taskJoin, years));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.groupBy(systemRoot.get("systemName"), taskJoin.get("status"), systemRoot.get("id"));

        TypedQuery<Object[]> query = entityManager.createQuery(cq);
        return query.getResultList().stream().map(row -> {
            TaskData taskData = new TaskData();
            taskData.setId(((Number) row[0]).longValue());
            taskData.setSystemName((String) row[1]);
            taskData.setStatus(((Number) row[2]).intValue());
            taskData.setTotal(((Number) row[3]).intValue());
            return taskData;
        }).collect(Collectors.toList());
    }
    
    private Predicate createYearPredicate(CriteriaBuilder cb, Join<?, TaskEntity> taskJoin, List<Integer> years) {
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

    @Override
    public List<TaskData> findTaskByPersonInChargePerWeek(List<Long> userIds, List<Integer> years, List<Integer> weeks) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<UserEntity> userRoot = cq.from(UserEntity.class);
        Join<UserEntity, TaskEntity> taskJoin = userRoot.join("tasks");

        Expression<Integer> week = cb.function("date_part", Integer.class, cb.literal("week"), taskJoin.get("expiredDate"));
        // Select clause
        cq.multiselect(
                userRoot.get("id"),
                userRoot.get("firstName"),
                week,
                cb.count(userRoot.get("id"))
        );
        List<Predicate> predicates = new ArrayList<>();
        if (!userIds.isEmpty()) {
            predicates.add(userRoot.get("id").in(userIds));
        }
        if (!years.isEmpty()) {
            predicates.add(createYearPredicate(cb, taskJoin, years));
        }
        if (!weeks.isEmpty()) {
            Predicate weekPredicate = week.in(weeks);
            predicates.add(weekPredicate);
        }
        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.groupBy(userRoot.get("firstName"),userRoot.get("id"), week);
        cq.orderBy(
                cb.asc(userRoot.get("id")),
                cb.asc(week)
        );
        TypedQuery<Object[]> query = entityManager.createQuery(cq);
        query.getResultList();
        return query.getResultList().stream().map(row -> {
            TaskData taskData = new TaskData();
            taskData.setId(((Number) row[0]).longValue());
            taskData.setFirstName((String) row[1]);
            taskData.setWeek(row[2] != null ? ((Number) row[2]).intValue() : 0);
            taskData.setTotal(((Number) row[3]).intValue());
            return taskData;
        }).collect(Collectors.toList());
    }
}
