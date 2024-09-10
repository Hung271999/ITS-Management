package com.sharp.vn.its.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "its_support_effort")
public class SupportEffortEntity extends BaseEntity{
    /**
     * The Id.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
     * The Effort.
     */
    @Column(name = "effort")
    private Double effort;

    /**
     * The Implementer.
     */
    @Column(name = "implementer")
    private String implementer;

    /**
     * The Participants.
     */
    @Column(name = "participants")
    private String participants;

    /**
     * The Status.
     */
    @Column(name = "status_id")
    private Integer status;

    /**
     * The Type.
     */
    @Column(name = "type_id")
    private Integer type;

    /**
     * The System.
     */
    @ManyToOne
    @JoinColumn(name = "system_id", referencedColumnName = "id")
    private SystemEntity system;

    /**
     * The Content.
     */
    @Column(name = "content")
    private String content;

    /**
     * The Note.
     */
    @Column(name = "note")
    private String note;

    /**
     * The Total effort.
     */
    @Column(name = "total_effort")
    private Double totalEffort;

    /**
     * The Created by.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedBy")
    private UserEntity createdBy;

    /** The updated by. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UpdatedBy")
    private UserEntity updatedBy;
}
