package com.sharp.vn.its.management.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharp.vn.its.management.dto.UserDTO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The type Task data dto.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDataDTO {
    /**
     * The Systems.
     */
    List<SystemDTO> systems;
    /**
     * The Types.
     */
    List<TaskTypeDTO> types;
    /**
     * The Status.
     */
    List<TaskStatusDTO> status;
    /**
     * The Users.
     */
    List<UserDTO> users;

    public TaskDataDTO(List<TaskTypeDTO> types, List<TaskStatusDTO> status, List<UserDTO> users,
            List<SystemDTO> systems) {
        this.types = types;
        this.status = status;
        this.users = users;
        this.systems = systems;
    }
}
