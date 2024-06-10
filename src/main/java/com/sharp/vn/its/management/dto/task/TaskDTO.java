package com.sharp.vn.its.management.dto.task;

import com.sharp.vn.its.management.dto.BaseDTO;
import com.sharp.vn.its.management.entity.TaskEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The type Task dto.
 */
@Getter
@Setter
@NotNull
public class TaskDTO extends BaseDTO {

    /**
     * The Task id.
     */
    private Long taskId;

    /**
     * The User id.
     */
    private Long userId;

    /**
     * The Receive date.
     */
    private LocalDateTime receiveDate;

    /**
     * The Expired date.
     */
    private LocalDateTime expiredDate;

    /**
     * The Start date.
     */
    private LocalDateTime startDate;

    /**
     * The End date.
     */
    private LocalDateTime endDate;

    /**
     * The Content.
     */
    private String content;

    /**
     * The Cost.
     */
    private Integer cost;

    /**
     * The Ticket number.
     */
    private String ticketNumber;

    /**
     * The Ticket url.
     */
    private String ticketURL;

    /**
     * The Note.
     */
    private String note;

    /**
     * The Status.
     */
    @NotNull(message = "Task status is required")
    private TaskStatusDTO status;

    /**
     * The Type.
     */
    @NotNull(message = "TTask type is required")
    private TaskTypeDTO type;

    /**
     * The System.
     */
    @NotNull(message = "System information is required")
    private SystemDTO system;


    /**
     * Instantiates a new Task dto.
     */
    public TaskDTO() {
    }

    /**
     * Instantiates a new Task dto.
     *
     * @param taskEntity the task entity
     */
    public TaskDTO(TaskEntity taskEntity) {
        this.taskId = taskEntity.getId();
        this.userId = getUserId();
        this.receiveDate = taskEntity.getReceiveDate();
        this.expiredDate = taskEntity.getExpiredDate();
        this.startDate = taskEntity.getStartDate();
        this.endDate = taskEntity.getEndDate();
        this.content = taskEntity.getContent();
        this.cost = taskEntity.getCost();
        this.ticketNumber = taskEntity.getTicketNumber();
        this.ticketURL = taskEntity.getTicketURL();
        this.note = taskEntity.getNote();
        this.status = new TaskStatusDTO(taskEntity.getStatus());
        this.type = new TaskTypeDTO(taskEntity.getType());
        this.system = new SystemDTO(taskEntity.getSystem());
    }
}
