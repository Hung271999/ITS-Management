package com.sharp.vn.its.management.dto.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharp.vn.its.management.entity.SystemEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * The type System dto.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemDTO {
    /**
     * The System id.
     */
    private Long systemId;


    /**
     * The System name.
     */
    @NotBlank(message = "System name must be not null or blank")
    private String systemName;

    /**
     * Instantiates a new System dto.
     *
     * @param system the system
     */
    public SystemDTO(SystemEntity system) {
        this.systemId = system.getId();
        this.systemName = system.getSystemName();
    }

    /**
     * The constant SORT_DEFAULT.
     */
    private static final String SORT_DEFAULT = "id";

    /**
     * The constant ORDER_DEFAULT.
     */
    private static final String ORDER_DEFAULT = "asc";

    /**
     * The Page no.
     */
    private int pageNo = 0;

    /**
     * The Page size.
     */
    private int pageSize = 10;

    /**
     * The sort field.
     */
    private String sortField;

    /**
     * The sort field.
     */
    private String sortOrder;

    /**
     * The search param.
     */
    private String searchParam;

    /**
     * Gets pageable.
     *
     * @return the pageable
     */
    @JsonIgnore
    public Pageable getPageable() {
        if (sortField == null || sortField.isEmpty()) {
            sortField = SORT_DEFAULT;
        }

        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = ORDER_DEFAULT;
        }

        Sort sort = Sort.by(sortField);

        if ("desc".equalsIgnoreCase(sortOrder)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        return PageRequest.of(pageNo, pageSize, sort);
    }
}
