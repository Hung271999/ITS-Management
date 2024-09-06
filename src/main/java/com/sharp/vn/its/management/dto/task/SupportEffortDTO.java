package com.sharp.vn.its.management.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharp.vn.its.management.entity.SupportEffortEntity;
import com.sharp.vn.its.management.filter.CriteriaSearchRequest;
import com.sharp.vn.its.management.util.DateTimeUtil;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Support effort dto.
 */
@Getter
@Setter
@NotNull
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupportEffortDTO {

    /**
     * The Support id.
     */
    private Long supportId;

    /**
     * The Start date.
     */
    private Long startDate;

    /**
     * The End date.
     */
    private Long endDate;

    /**
     * The Created date.
     */
    private Long createdDate;

    /**
     * The Updated date.
     */
    private Long updatedDate;

    /**
     * The Effort.
     */
    private Double effort;

    /**
     * The Implementer.
     */
    private String implementer;

    /**
     * The Participants.
     */
    private String participants;

    /**
     * The Status.
     */
    private Integer status;

    /**
     * The Type.
     */
    private Integer type;

    /**
     * The System.
     */
    private Long system;

    /**
     * The System Name.
     */
    private String systemName;

    /**
     * The Content.
     */
    private String content;

    /**
     * The Note.
     */
    private String note;

    /**
     * The Comprehensive.
     */
    private Double totalEffort;



    /**
     * The Filter.
     */
    private CriteriaSearchRequest filter = new CriteriaSearchRequest();

    /**
     * Instantiates a new Support effort dto.
     */
    public SupportEffortDTO() {
    }

    /**
     * Instantiates a new Support effort dto.
     *
     * @param supportEffortEntity the support effort entity
     */
    public SupportEffortDTO(SupportEffortEntity supportEffortEntity) {
        this.supportId = supportEffortEntity.getId();
        this.startDate = DateTimeUtil.toEpochMilli(supportEffortEntity.getStartDate());
        this.endDate = DateTimeUtil.toEpochMilli(supportEffortEntity.getEndDate());
        this.createdDate = DateTimeUtil.toEpochMilli(supportEffortEntity.getCreatedDate());
        this.updatedDate = DateTimeUtil.toEpochMilli(supportEffortEntity.getUpdatedDate());
        this.effort = supportEffortEntity.getEffort();
        this.implementer = supportEffortEntity.getImplementer();
        this.participants = supportEffortEntity.getParticipants();
        this.status = supportEffortEntity.getStatus();
        this.type = supportEffortEntity.getType();
        this.system = supportEffortEntity.getSystem() != null ? supportEffortEntity.getSystem().getId() : 0;
        this.systemName = supportEffortEntity.getSystem() != null ? supportEffortEntity.getSystem().getSystemName() : "";
        this.content = supportEffortEntity.getContent();
        this.note = supportEffortEntity.getNote();
        this.totalEffort = supportEffortEntity.getTotalEffort();
        this.filter = null;
    }
}
