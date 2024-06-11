package com.sharp.vn.its.management.service;


import com.sharp.vn.its.management.dto.UserDTO;
import com.sharp.vn.its.management.dto.task.SystemDTO;
import com.sharp.vn.its.management.dto.task.TaskDTO;
import com.sharp.vn.its.management.dto.task.TaskDataDTO;
import com.sharp.vn.its.management.dto.task.TaskStatusDTO;
import com.sharp.vn.its.management.dto.task.TaskTypeDTO;
import com.sharp.vn.its.management.entity.SystemEntity;
import com.sharp.vn.its.management.entity.TaskEntity;
import com.sharp.vn.its.management.entity.TaskStatusEntity;
import com.sharp.vn.its.management.entity.TaskTypeEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.exception.ObjectNotFoundException;
import com.sharp.vn.its.management.repositories.SystemRepository;
import com.sharp.vn.its.management.repositories.TaskRepository;
import com.sharp.vn.its.management.repositories.TaskStatusRepository;
import com.sharp.vn.its.management.repositories.TaskTypeRepository;
import com.sharp.vn.its.management.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Task service.
 */
@Service
@Slf4j
public class TaskService extends BaseService {

    /**
     * The Task status repository.
     */
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    /**
     * The Task type repository.
     */
    @Autowired
    private TaskTypeRepository taskTypeRepository;

    /**
     * The System repository.
     */
    @Autowired
    private SystemRepository systemRepository;

    /**
     * The Task repository.
     */
    @Autowired
    private TaskRepository taskRepository;

    /**
     * The User repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * The Authentication service.
     */
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Gets task data.
     *
     * @return the task data
     */
    public TaskDataDTO getTaskData() {
        log.info("Fetching task data...");
        final List<TaskStatusDTO> status =
                taskStatusRepository.findAll().stream().map(TaskStatusDTO::new).toList();
        final List<TaskTypeDTO> types =
                taskTypeRepository.findAll().stream().map(TaskTypeDTO::new).toList();
        final List<SystemDTO> systems =
                systemRepository.findAll().stream().map(SystemDTO::new).toList();
        final List<UserDTO> users = userRepository.findAll().stream()
                .map(userEntity -> new UserDTO(userEntity.getId(), userEntity.getUsername()))
                .toList();
        log.info("Task data fetched successfully.");
        return new TaskDataDTO(types, status, users, systems);
    }

    /**
     * Gets all tasks.
     *
     * @return the all tasks
     */
    public List<TaskDTO> getAllTasks(TaskDTO request) {
        log.info("Fetching all tasks...");
        final List<TaskDTO> tasks = taskRepository.findAll().stream().map(TaskDTO::new).toList();
        log.info("All tasks fetched successfully.");
        return tasks;
    }

    /**
     * Gets task detail.
     *
     * @param id the id
     * @return the task detail
     */
    public TaskDTO getTaskDetail(Long id) {
        if (id == null) {
            throw new DataValidationException("Task id not found");
        }
        log.info("Fetching task detail for id: {}", id);
        final TaskDTO taskDTO = new TaskDTO(taskRepository.findById(id).orElseThrow(() -> {
            return new ObjectNotFoundException("Task not found with id: " + id);
        }));
        log.info("Task detail fetched successfully for id: {}", id);
        return taskDTO;
    }

    /**
     * Delete task.
     *
     * @param id the id
     */
    public void deleteTask(Long id) {
        if (id == null) {
            throw new DataValidationException("Task id not found");
        }
        taskRepository.deleteById(id);
        log.info("Task with id {} deleted successfully.", id);
    }

    /**
     * Save task task DTO.
     *
     * @param taskDTO the task DTO
     * @return the task DTO
     */
    public TaskDTO saveTask(TaskDTO taskDTO) {
        log.info("Saving task...");
        final Long taskId = taskDTO.getTaskId();
        final Long typeId = taskDTO.getType().getTypeId();
        final Long statusId = taskDTO.getStatus().getStatusId();
        final Long systemId = taskDTO.getSystem().getSystemId();

        TaskEntity taskEntity = new TaskEntity();
        // update when task id is not null
        if (taskId != null) {
            taskEntity = taskRepository.findById(taskId).orElseThrow(
                    () -> new DataValidationException("Task not found with id: " + taskId));
        }
        final String userName = authenticationService.getUser().getUsername();
        if (userName == null) {
            throw new ObjectNotFoundException("User not found");
        }
        final SystemEntity system = systemRepository.findById(systemId).orElseThrow(
                () -> new ObjectNotFoundException("System not found with id: " + systemId));
        final TaskTypeEntity taskType = taskTypeRepository.findById(typeId).orElseThrow(
                () -> new ObjectNotFoundException("Task type not found with id: " + typeId));
        final TaskStatusEntity taskStatus = taskStatusRepository.findById(statusId).orElseThrow(
                () -> new ObjectNotFoundException("Task status not found with id: " + statusId));
        final UserEntity userEntity = userRepository.findById(taskDTO.getUserId()).orElseThrow(
                () -> new ObjectNotFoundException(
                        "User not found with id: " + taskDTO.getUserId()));
        // set properties
        BeanUtils.copyProperties(taskDTO, taskEntity);
        taskEntity.setSystem(system);
        taskEntity.setType(taskType);
        taskEntity.setUser(userEntity);
        taskEntity.setStatus(taskStatus);
        taskEntity.setCreatedBy(userName);
        taskEntity.setUpdatedBy(userName);
        taskEntity = taskRepository.save(taskEntity);
        log.info("Task saved successfully.");
        return new TaskDTO(taskEntity);
    }



}
