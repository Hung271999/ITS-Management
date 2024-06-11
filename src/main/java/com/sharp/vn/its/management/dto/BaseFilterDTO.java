package com.sharp.vn.its.management.dto;

import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.exception.ITSException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Base filter dto.
 */
@Getter
@Setter
public abstract class BaseFilterDTO implements Serializable {
    /**
     * The constant SORT_DEFAULT.
     */
    private static final String SORT_DEFAULT = "id";
    /**
     * The constant ORDER_DEFAULT.
     */
    private static final String ORDER_DEFAULT = "asc";
    /**
     * The Page.
     */
    private Integer page = 0;
    /**
     * The Size.
     */
    private Integer size = 10;
    /**
     * The Filter.
     */
    private List<FilterDTO> filter = new ArrayList<>();

    /**
     * Gets pageable.
     *
     * @return the pageable
     */
    public Pageable getPageable() {
        init();
        List<Sort.Order> orders = new ArrayList<>();
        for (FilterDTO filterDTO : filter) {
            Sort.Direction direction = Sort.Direction.fromString(filterDTO.getOrder());
            orders.add(new Sort.Order(direction, filterDTO.getSort()));
        }
        return PageRequest.of(page, size, Sort.by(orders));
    }

    /**
     * Init.
     */
    private void init() {
        if (!filter.isEmpty()) {
            return;
        }
        filter.add(new FilterDTO(ORDER_DEFAULT, SORT_DEFAULT));

    }
}
