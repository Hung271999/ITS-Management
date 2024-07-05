package com.sharp.vn.its.management.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharp.vn.its.management.entity.SystemEntity;
import com.sharp.vn.its.management.filter.CriteriaSearchRequest;
import com.sharp.vn.its.management.util.DateTimeUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * The Update by.
     */
    private String updateBy;

    /**
     * The Created date.
     */
    private Long createdDate;

    /**
     * The Updated date.
     */
    private Long updatedDate;

    /**
     * Instantiates a new System dto.
     *
     * @param system the system
     */
    public SystemDTO(SystemEntity system) {
        this.systemId = system.getId();
        this.systemName = system.getSystemName();
        this.createdDate = DateTimeUtil.toEpochMilli(system.getCreatedDate());
        this.updatedDate = DateTimeUtil.toEpochMilli(system.getUpdatedDate());
        this.updateBy = system.getUpdatedBy().getFullName();
    }

    /**
     * The Filter.
     */
    private CriteriaSearchRequest filter = new CriteriaSearchRequest();
}
