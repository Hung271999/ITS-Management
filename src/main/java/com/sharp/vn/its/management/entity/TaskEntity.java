package com.sharp.vn.its.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "its_task")
public class TaskEntity extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receive_date")
    private LocalDateTime receiveDate;

    @Column(name = "expired_date")
    private LocalDateTime expiredDate;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "content")
    private String content;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @Column(name = "ticket_url")
    private String ticketURL;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private TaskStatusEntity status;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private TaskTypeEntity type;

    @ManyToOne
    @JoinColumn(name = "system_id", referencedColumnName = "id")
    private SystemEntity system;

    @ManyToOne
    @JoinColumn(name = "user_Id", referencedColumnName = "id")
    private UserEntity user;
}
