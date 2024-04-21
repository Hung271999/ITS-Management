package com.sharp.vn.its.management.service;

import com.sharp.vn.its.management.dto.*;
import com.sharp.vn.its.management.entity.*;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskTypeRepository taskTypeRepository;
    @Autowired
    private SystemRepository systemRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    public TaskInforDto loadTaskInformation() {
        TaskInforDto taskInfor = new TaskInforDto();

        List<TaskStatusDto> status = taskStatusRepository.findAll().stream()
                .map(TaskStatusDto::new)
                .toList();

        List<TaskTypeDto> types = taskTypeRepository.findAll().stream()
                .map(TaskTypeDto::new)
                .toList();

        taskInfor.setTypes(types);

        List<SystemDto> systems = systemRepository.findAll().stream()
                .map(SystemDto::new)
                .toList();
        taskInfor.setSystems(systems);

        List<UserDto> users = userRepository.findAll().stream().map(userEntity -> {
            UserDto userDto = new UserDto();
            userDto.setId(userEntity.getId());
            userDto.setUserName(userEntity.getUsername());
            return userDto;
        }).collect(Collectors.toList());
        taskInfor.setUsers(users);
        return taskInfor;
    }

    public TaskDto saveTask(TaskDto taskDto) {
        if (taskDto.getStatus() == null) {
            throw new DataValidationException("Status id invalid");
        }
        if (taskDto.getSystem() == null) {
            throw new DataValidationException("System id invalid");
        }
        if (taskDto.getType() == null) {
            throw new DataValidationException("Type id invalid");
        }

        if (taskDto.getUser() == null) {
            throw new DataValidationException("User id invalid");
        }
        TaskEntity taskEntity = new TaskEntity();
        if (taskDto.getId() != null) {
            taskEntity = taskRepository.findById(taskDto.getId()).orElseThrow(() -> new DataValidationException("Task id not found"));
        }

        SystemEntity system = systemRepository.findById(taskDto.getSystem().getSystemId())
                .orElseThrow(() -> new DataValidationException("System not found"));

        TaskTypeEntity taskType = taskTypeRepository.findById(taskDto.getType().getTypeId())
                .orElseThrow(() -> new DataValidationException("Task type not found"));

        TaskStatusEntity taskStatus = taskStatusRepository.findById(taskDto.getStatus().getStatusId())
                .orElseThrow(() -> new DataValidationException("Task status not found"));

        UserEntity userEntity = userRepository.findById(taskDto.getUser().getId())
                .orElseThrow(() -> new DataValidationException("User id not found"));

        BeanUtils.copyProperties(taskDto, taskEntity);
        taskEntity.setSystem(system);
        taskEntity.setType(taskType);
        String userName = authenticationService.getUser().getUsername();
        taskEntity.setCreatedBy(userName);
        taskEntity.setUpdatedBy(userName);
        taskEntity.setUser(userEntity);
        taskEntity.setStatus(taskStatus);
        taskEntity = taskRepository.save(taskEntity);
        taskDto.setId(taskEntity.getId());
        return taskDto;
    }

    public void deleteTask(Long id) {
        if (id == null) {
            throw new DataValidationException("Task id invalid");
        }
        taskRepository.deleteById(id);
    }

    public List<TaskDto> loadAllTask() {
        return taskRepository
                .findAll().stream()
                .map(TaskDto::new)
                .toList();
    }

    public TaskDto getTaskDetail(Long id) {
        if (id == null) {
            throw new DataValidationException("Task id invalid");
        }
        return new TaskDto(taskRepository.findById(id).get());
    }


}
