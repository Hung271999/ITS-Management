package com.sharp.vn.its.management.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharp.vn.its.management.dto.system.SystemDTO;
import com.sharp.vn.its.management.entity.TaskEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.filter.CriteriaSearchRequest;
import com.sharp.vn.its.management.util.DateTimeUtil;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Task dto.
 */
@Getter
@Setter
@NotNull
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDTO {

    /**
     * The Task id.
     */
    private Long taskId;

    /**
     * The User id.
     */
    private Long userId;

    /**
     * The User name.
     */
    private String userName;

    /**
     * The Full name.
     */
    private String fullName;

    /**
     * The Receive date.
     */
    private Long receiveDate;

    /**
     * The Expired date.
     */
    private Long expiredDate;

    /**
     * The Start date.
     */
    private Long startDate;

    /**
     * The End date.
     */
    private Long endDate;

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
    private Integer status;

    /**
     * The Type.
     */
    @NotNull(message = "Task type is required")
    private Integer type;

    /**
     * The System.
     */
    @NotNull(message = "System information is required")
    private Long system;

    /**
     * The System name.
     */
    private String systemName;

    /**
     * The Created date.
     */
    private Long createdDate;

    /**
     * The Updated date.
     */
    private Long updatedDate;
    /**
     * The Filter.
     */
    private CriteriaSearchRequest filter = new CriteriaSearchRequest();


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
        this.receiveDate = DateTimeUtil.toEpochSeconds(taskEntity.getReceiveDate());
        this.expiredDate = DateTimeUtil.toEpochSeconds(taskEntity.getExpiredDate());
        this.startDate = DateTimeUtil.toEpochSeconds(taskEntity.getStartDate());
        this.endDate = DateTimeUtil.toEpochSeconds(taskEntity.getEndDate());
        this.content = taskEntity.getContent();
        this.cost = taskEntity.getCost();
        this.ticketNumber = taskEntity.getTicketNumber();
        this.ticketURL = taskEntity.getTicketURL();
        this.note = taskEntity.getNote();
        this.status = taskEntity.getStatus();
        this.type = taskEntity.getType();
        this.system = taskEntity.getSystem().getId();
        this.systemName = taskEntity.getSystem().getSystemName();
        UserEntity userEntity = taskEntity.getPersonInCharge();
        if (userEntity == null) {
            return;
        }
        this.fullName = userEntity.getFirstName() + " " + userEntity.getLastName();
        this.userName = userEntity.getUsername();
        this.userId = userEntity.getId();
        this.createdDate = DateTimeUtil.toEpochSeconds(taskEntity.getCreatedDate());
        this.updatedDate = DateTimeUtil.toEpochSeconds(taskEntity.getUpdatedDate());
        this.filter = null;
    }
}
