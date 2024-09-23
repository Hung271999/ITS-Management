package com.sharp.vn.its.management.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * The type Team capacity entity.
 */
@Entity
@Data
@Table(name = "its_team_capacity")
public class TeamCapacityEntity extends BaseEntity {
    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * The Start date.
     */
    @Column(name = "start_date")
    private LocalDateTime startDate;

    /**
     * The End date.
     */
    @Column(name = "end_date")
    private LocalDateTime endDate;

    /**
     * The Head count.
     */
    @Column(name = "head_count")
    private int headCount;

    /**
     * The Total hours.
     */
    @Column(name = "total_hours")
    private double totalHours;

    /**
     * The Time off.
     */
    @Column(name = "time_off")
    private double timeOff;

    /**
     * The Reports.
     */
    @Column(name = "reports")
    private double reports;

    /**
     * The Actual capacity.
     */
    @Column (name = "actual_capacity")
    private double actualCapacity;

    /**
     * The Note.
     */
    @Column(name = "note")
    private String note;

}