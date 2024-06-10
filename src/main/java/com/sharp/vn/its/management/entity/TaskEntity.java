package com.sharp.vn.its.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The type Task entity.
 */
@Getter
@Setter
@Entity
@Table(name = "its_task")
public class TaskEntity extends BaseEntity {
    /**
     * The Id.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The Receive date.
     */
    @Column(name = "receive_date")
    private LocalDateTime receiveDate;

    /**
     * The Expired date.
     */
    @Column(name = "expired_date")
    private LocalDateTime expiredDate;

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
     * The Content.
     */
    @Column(name = "content")
    private String content;

    /**
     * The Cost.
     */
    @Column(name = "cost")
    private Integer cost;

    /**
     * The Ticket number.
     */
    @Column(name = "ticket_number")
    private String ticketNumber;

    /**
     * The Ticket url.
     */
    @Column(name = "ticket_url")
    private String ticketURL;

    /**
     * The Note.
     */
    @Column(name = "note")
    private String note;

    /**
     * The Status.
     */
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private TaskStatusEntity status;

    /**
     * The Type.
     */
    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private TaskTypeEntity type;

    /**
     * The System.
     */
    @ManyToOne
    @JoinColumn(name = "system_id", referencedColumnName = "id")
    private SystemEntity system;

    /**
     * The User.
     */
    @ManyToOne
    @JoinColumn(name = "user_Id", referencedColumnName = "id")
    private UserEntity user;
}
