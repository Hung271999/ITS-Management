package com.sharp.vn.its.management.util;

import com.sharp.vn.its.management.constants.*;
import com.sharp.vn.its.management.filter.*;

import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public final class CriteriaUtil {

    public static Predicate buildPredicate(final CriteriaBuilder criteriaBuilder,
            final Root<?> root,
            final CriteriaFilterItem criteriaFilterItem) {
        final List<Predicate> predicates = new ArrayList<>();
        if (criteriaFilterItem == null) {
            return null;
        }
        final String fieldName = criteriaFilterItem.getFieldName();
        final int type = criteriaFilterItem.getType();
        CollectionUtils.addIfNotEmptyOrNull(predicates, buildBooleanFilter(criteriaBuilder, root,
                criteriaFilterItem.getFilterBooleanValue(),
                fieldName));

        CollectionUtils.addIfNotEmptyOrNull(predicates, buildDateTimeFilter(criteriaBuilder, root,
                criteriaFilterItem.getFilterDatetimeValue(),
                fieldName, type));
        CollectionUtils.addIfNotEmptyOrNull(predicates,
                buildNumberFilter(criteriaBuilder, root, criteriaFilterItem.getFilterNumberValue(),
                        fieldName, type));
        CollectionUtils.addIfNotEmptyOrNull(predicates,
                buildTextFilter(criteriaBuilder, root, criteriaFilterItem.getFilterTextValue(),
                        fieldName, type));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private static Predicate buildBooleanFilter(final CriteriaBuilder criteriaBuilder,
            final Root<?> root,
            final BooleanCriteriaFilter booleanCriteriaFilter,
            final String fieldName) {
        if (booleanCriteriaFilter == null) {
            return null;
        }
        Predicate predicate =
                criteriaBuilder.equal(root.get(fieldName), booleanCriteriaFilter.getValue());
        return predicate;
    }

    private static Predicate buildDateTimeFilter(final CriteriaBuilder criteriaBuilder,
            final Root<?> root,
            final DateTimeCriteriaFilter dateTimeCriteriaFilter,
            final String fieldName, int type) {
        if (dateTimeCriteriaFilter == null) {
            return null;
        }
        final DateTimeFilterType operation =
                DateTimeFilterType.fromValue(type);
        switch (operation) {
            case DATE_TIME_IS:
                return criteriaBuilder.equal(root.get(fieldName),
                        DateTimeUtil.toLocalDateTime(dateTimeCriteriaFilter.getStartTime()));

            case DATE_TIME_IS_NOT:
                return criteriaBuilder.notEqual(root.get(fieldName),
                        DateTimeUtil.toLocalDateTime(dateTimeCriteriaFilter.getStartTime()));

            case DATE_TIME_IS_BEFORE:
                return criteriaBuilder.lessThan(root.get(fieldName),
                        DateTimeUtil.toLocalDateTime(dateTimeCriteriaFilter.getStartTime()));

            case DATE_TIME_IS_AFTER:
                return criteriaBuilder.greaterThan(root.get(fieldName),
                        DateTimeUtil.toLocalDateTime(dateTimeCriteriaFilter.getStartTime()));
            case DATE_TIME_IS_BETWEEN:
                return criteriaBuilder.between(root.get(fieldName),
                        DateTimeUtil.toLocalDateTime(dateTimeCriteriaFilter.getStartTime()),
                        DateTimeUtil.toLocalDateTime(dateTimeCriteriaFilter.getEndTime()));
            case DATE_TIME_IS_NOT_BETWEEN:
                return criteriaBuilder.not(criteriaBuilder.between(root.get(fieldName),
                        DateTimeUtil.toLocalDateTime(dateTimeCriteriaFilter.getStartTime()),
                        DateTimeUtil.toLocalDateTime(dateTimeCriteriaFilter.getEndTime())));
            default:
                return null;
        }
    }

    private static Predicate buildNumberFilter(final CriteriaBuilder criteriaBuilder,
            final Root<?> root,
            final NumberCriteriaFilter numberCriteriaFilter,
            final String fieldName, int type) {
        if (numberCriteriaFilter == null) {
            return null;
        }
        final List<Predicate> predicates = new ArrayList<>();
        final NumberFilterType operation =
                NumberFilterType.fromValue(type);
        switch (operation) {
            case IS_EQUAL_TO:
                return criteriaBuilder.equal(root.get(fieldName),
                        numberCriteriaFilter.getToValue());
            case IS_NOT_EQUAL_TO:
                return criteriaBuilder.notEqual(root.get(fieldName),
                        numberCriteriaFilter.getToValue());
            case IS_GREATER_THAN:
                return criteriaBuilder.greaterThan(root.get(fieldName),
                        (Comparable) numberCriteriaFilter.getToValue());
            case IS_GREATER_OR_EQUAL_TO:
                return criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName),
                        (Comparable) numberCriteriaFilter.getToValue());
            case IS_LESS_THAN:
                return criteriaBuilder.lessThan(root.get(fieldName),
                        (Comparable) numberCriteriaFilter.getToValue());
            case IS_LESS_THAN_OR_EQUAL_TO:
                return criteriaBuilder.lessThanOrEqualTo(root.get(fieldName),
                        (Comparable) numberCriteriaFilter.getToValue());
            case IS_BETWEEN:
                return criteriaBuilder.between(root.get(fieldName),
                        (Comparable) numberCriteriaFilter.getFromValue(),
                        (Comparable) numberCriteriaFilter.getToValue());
            case IS_NOT_BETWEEN:
                return criteriaBuilder.not(criteriaBuilder.between(root.get(fieldName),
                        (Comparable) numberCriteriaFilter.getFromValue(),
                        (Comparable) numberCriteriaFilter.getToValue()));
            default:
                return null;
        }
    }

    private static Predicate buildTextFilter(final CriteriaBuilder criteriaBuilder,
            final Root<?> root,
            final TextCriteriaFilter filterTextValue,
            final String fieldName, int type) {
        if (filterTextValue == null) {
            return null;
        }
        final List<Predicate> predicates = new ArrayList<>();
        final String filterValue = filterTextValue.getValue();
        final TextFilterType operation =
                TextFilterType.fromValue(type);
        switch (operation) {
            case TEXT_CONTAINS:
                return criteriaBuilder.like(root.get(fieldName), "%" + filterValue + "%");
            case TEXT_DOES_NOT_CONTAINS:
                return
                        criteriaBuilder.notLike(root.get(fieldName), "%" + filterValue + "%");
            case TEXT_STARTS_WITH:
                return criteriaBuilder.like(root.get(fieldName), filterValue + "%");
            case TEXT_ENDS_WITH:
                return criteriaBuilder.like(root.get(fieldName), "%" + filterValue);
            case TEXT_IS_EXACTLY:
                return criteriaBuilder.equal(root.get(fieldName), filterValue);
            case TEXT_IS_NOT:
                return criteriaBuilder.notEqual(root.get(fieldName), filterValue);
            default:
                return null;
        }
    }

    public static Predicate buildCombinedPredicate(CriteriaBuilder criteriaBuilder,
            FilterType filterType,
            Predicate... predicates) {
        if (predicates == null) {
            return null;
        }
        predicates = Arrays.stream(predicates)
                .filter(Objects::nonNull)
                .toArray(Predicate[]::new);
        if (predicates.length == 0) {
            return null;
        }
        switch (filterType) {
            case AND:
                return criteriaBuilder.and(predicates);
            case OR:
                return criteriaBuilder.or(predicates);
            case NOT:
                return criteriaBuilder.not(criteriaBuilder.and(predicates));
            default:
                throw new IllegalArgumentException("Unknown FilterType: " + filterType);
        }
    }

    public static List<Order> buildOrders(CriteriaBuilder criteriaBuilder, Root<?> root,
                                   Map<String, SortCriteria> sort) {
        return sort.values().stream()
                .map(sortCriteria -> {
                    if (sortCriteria.getSortType().equals(SortType.ASC.getText())) {
                        return criteriaBuilder.asc(root.get(sortCriteria.getFieldName()));
                    }
                    return criteriaBuilder.desc(root.get(sortCriteria.getFieldName()));
                })
                .collect(Collectors.toList());
    }
}
