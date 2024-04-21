package com.sharp.vn.its.management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharp.vn.its.management.entity.SystemEntity;
import com.sharp.vn.its.management.entity.TaskStatusEntity;
import com.sharp.vn.its.management.entity.TaskTypeEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TaskInforDto {
    List<SystemDto> systems;
    List<TaskTypeDto> types;
    List<TaskStatusDto> status;
    List<UserDto> users;
}
