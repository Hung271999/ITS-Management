package com.sharp.vn.its.management.dto.capacity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

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
    private long id;
    /**
     * The Start date.
     */
    private LocalDateTime startDate;
    /**
     * The End date.
     */
    private LocalDateTime endDate;
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
    public TeamCapacityDTO()
{}


    /**
     * Instantiates a new Team capacity dto.
     *
     * @param actualCapacity the actual capacity
     * @param endDate        the end date
     * @param headCount      the head count
     * @param id             the id
     * @param note           the note
     * @param reports        the reports
     * @param startDate      the start date
     * @param timeOff        the time off
     * @param totalHours     the total hours
     */
    public TeamCapacityDTO(double actualCapacity, LocalDateTime endDate, int headCount, long id, String note, double reports, LocalDateTime startDate, double timeOff, double totalHours) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.headCount = headCount;
        this.totalHours = totalHours;
        this.timeOff = timeOff;
        this.reports = reports;
        this.actualCapacity = totalHours-timeOff-reports;
        this.note = note;
    }
}
