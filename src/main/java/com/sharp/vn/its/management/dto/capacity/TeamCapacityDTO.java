package com.sharp.vn.its.management.dto.capacity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharp.vn.its.management.entity.TeamCapacityEntity;
import com.sharp.vn.its.management.filter.CriteriaSearchRequest;
import com.sharp.vn.its.management.util.DateTimeUtil;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * The type Team capacity dto.
 */
@Data
@NotNull
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamCapacityDTO {
    /**
     * The Id.
     */
    private long capacityId;

    /**
     * The Start date.
     */
    private Long startDate;

    /**
     * The End date.
     */
    private Long endDate;

    /**
     * The Head count.
     */
    private int headCount;

    /**
     * The Total hours.
     */
    private double totalHours;

    /**
     * The Time off.
     */
    private double timeOff;

    /**
     * The Reports.
     */
    private double reports;

    /**
     * The Actual capacity.
     */
    private double actualCapacity;

    /**
     * The Note.
     */
    private String note;

    /**
     * Instantiates a new Team capacity dto.
     */
    public TeamCapacityDTO() {}

    /**
     * Instantiates a new Team capacity dto.
     *
     * @param entity the entity
     */
    public TeamCapacityDTO(TeamCapacityEntity entity) {
        this.capacityId = entity.getId();
        this.actualCapacity = entity.getActualCapacity();
        this.endDate = DateTimeUtil.toEpochMilli(entity.getEndDate());
        this.headCount = entity.getHeadCount();
        this.note = entity.getNote();
        this.reports = entity.getReports();
        this.startDate =DateTimeUtil.toEpochMilli(entity.getStartDate());
        this.timeOff = entity.getTimeOff();
        this.totalHours = entity.getTotalHours();
    }

    /**
     * The Filter.
     */
    private CriteriaSearchRequest filter = new CriteriaSearchRequest();
}
