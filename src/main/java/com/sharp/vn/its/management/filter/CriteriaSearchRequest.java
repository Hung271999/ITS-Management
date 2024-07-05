package com.sharp.vn.its.management.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Criteria search request.
 */
@Data
@NoArgsConstructor
public class CriteriaSearchRequest {

    /**
     * The constant SORT_DEFAULT.
     */
    private static final String SORT_DEFAULT = "id";

    /**
     * The constant ORDER_DEFAULT.
     */
    private static final String ORDER_DEFAULT = "desc";
    /**
     * The Search param.
     */
    private Map<String, CriteriaFilterItem> searchParam;

    /**
     * The Sort.
     */
    private Map<String, SortCriteria> sort;

    /**
     * The Search Keyword.
     */
    private String searchKeyword;
    /**
     * The Page no.
     */
    private int pageNo = 0;

    /**
     * The Page size.
     */
    private int pageSize = 10;

    /**
     * Gets pageable.
     *
     * @return the pageable
     */
    @JsonIgnore
    public Pageable getPageable() {
        init();
        List<Sort.Order> orders = sort.entrySet().stream()
                .map(entry -> {
                    SortCriteria sortCriteria = entry.getValue();
                    Sort.Direction direction =
                            Sort.Direction.fromString(sortCriteria.getSortType());
                    return new Sort.Order(direction,sortCriteria.getFieldName());
                })
                .collect(Collectors.toList());

        Sort sortObject = Sort.by(orders);
        return PageRequest.of(pageNo, pageSize, sortObject);
    }

    /**
     * Init.
     */
    private void init() {
        if (sort != null) {
            return;
        }
        sort = new HashMap<>();
        SortCriteria sortCriteria = new SortCriteria();
        sortCriteria.setSortType(ORDER_DEFAULT);
        sortCriteria.setFieldName(SORT_DEFAULT);
        sort.put(SORT_DEFAULT, sortCriteria);
    }
}
