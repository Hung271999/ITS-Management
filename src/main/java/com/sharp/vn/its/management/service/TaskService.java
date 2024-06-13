package com.sharp.vn.its.management.service;


import com.sharp.vn.its.management.constants.FilterType;
import com.sharp.vn.its.management.constants.TaskStatus;
import com.sharp.vn.its.management.constants.TaskType;
import com.sharp.vn.its.management.dto.task.TaskDTO;
import com.sharp.vn.its.management.entity.SystemEntity;
import com.sharp.vn.its.management.entity.TaskEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.exception.ObjectNotFoundException;
import com.sharp.vn.its.management.filter.CriteriaFilterItem;
import com.sharp.vn.its.management.repositories.SystemRepository;
import com.sharp.vn.its.management.repositories.TaskRepository;
import com.sharp.vn.its.management.repositories.UserRepository;
import com.sharp.vn.its.management.util.CollectionUtils;
import com.sharp.vn.its.management.util.DateTimeUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.sharp.vn.its.management.util.CriteriaUtil.buildCombinedPredicate;
import static com.sharp.vn.its.management.util.CriteriaUtil.buildPredicate;

/**
 * The type Task service.
 */
@Service
@Slf4j
public class TaskService extends BaseService {

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
     * Gets all tasks.
     *
     * @return the all tasks
     */
    public Page<TaskDTO> getAllTasks(TaskDTO request) {
        log.info("Fetching all tasks...");
        Map<String, CriteriaFilterItem> searchParam = request.getFilter().getSearchParam();
        Specification<TaskEntity> specification =
                (Root<TaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    List<Predicate> subPredicates = new ArrayList<>();
                    if (searchParam == null) {
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    }
                    CollectionUtils.addIfNotEmptyOrNull(predicates,
                            buildPredicate(criteriaBuilder, root, searchParam.get("status")));

                    CollectionUtils.addIfNotEmptyOrNull(predicates,
                            buildPredicate(criteriaBuilder, root, searchParam.get("type")));


                    CollectionUtils.addIfNotEmptyOrNull(predicates,
                            buildCombinedPredicate(criteriaBuilder, FilterType.OR,
                                    buildPredicate(criteriaBuilder, root,
                                            searchParam.get("ticketNumber")),
                                    buildPredicate(criteriaBuilder, root,
                                            searchParam.get("content"))));

                    CriteriaFilterItem personInCharge = searchParam.get("personInCharge");
                    if (personInCharge != null) {
                        Join<TaskEntity, UserEntity> userJoin = root.join("personInCharge");
                        CollectionUtils.addIfNotEmptyOrNull(predicates,
                                criteriaBuilder.equal(userJoin.get("id"),
                                        personInCharge.getFilterNumberValue().getToValue()));
                    }
                    CriteriaFilterItem system = searchParam.get("system");
                    if (system != null) {
                        Join<TaskEntity, SystemEntity> systemJoin = root.join("system");
                        CollectionUtils.addIfNotEmptyOrNull(predicates,
                                criteriaBuilder.equal(systemJoin.get("id"),
                                        system.getFilterNumberValue().getToValue()));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };
        Page<TaskEntity> pageable = taskRepository.findAll(specification, request.getFilter()
                .getPageable());
        log.info("All tasks fetched successfully.");
        return pageable.map(TaskDTO::new);
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
        final Long systemId = taskDTO.getSystem();

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

        final TaskType taskType = TaskType.valueOf(taskDTO.getType());

        final TaskStatus taskStatus = TaskStatus.valueOf(taskDTO.getStatus());

        final UserEntity personInCharge = userRepository.findById(taskDTO.getUserId()).orElseThrow(
                () -> new ObjectNotFoundException(
                        "User not found with id: " + taskDTO.getUserId()));
        final UserEntity userEntity = userRepository.findById(authenticationService.getUser()
                .getId()).get();
        // set properties
        BeanUtils.copyProperties(taskDTO, taskEntity);
        taskEntity.setStartDate(DateTimeUtil.toLocalDateTime(taskDTO.getStartDate()));
        taskEntity.setEndDate(DateTimeUtil.toLocalDateTime(taskDTO.getEndDate()));
        taskEntity.setReceiveDate(DateTimeUtil.toLocalDateTime(taskDTO.getReceiveDate()));
        taskEntity.setExpiredDate(DateTimeUtil.toLocalDateTime(taskDTO.getExpiredDate()));
        taskEntity.setSystem(system);
        taskEntity.setType(taskType.getType());
        taskEntity.setPersonInCharge(personInCharge);
        taskEntity.setStatus(taskStatus.getStatus());
        taskEntity.setCreatedBy(userEntity);
        taskEntity.setUpdatedBy(userEntity);
        taskEntity = taskRepository.save(taskEntity);
        log.info("Task saved successfully.");
        return new TaskDTO(taskEntity);
    }



}
