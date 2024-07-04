package com.sharp.vn.its.management.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotNull
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestCloneTaskDTO {
    /**
     * The Task id.
     */
    private Long taskId;

    /**
     * The Number of tasks.
     */
    private int numberOfCloneTask;
}
