package com.sharp.vn.its.management.service;


import com.sharp.vn.its.management.constants.*;
import com.sharp.vn.its.management.dto.chart.TotalDTO;
import com.sharp.vn.its.management.dto.chart.TotalItem;
import com.sharp.vn.its.management.dto.task.TaskDTO;
import com.sharp.vn.its.management.dto.chart.ChartDTO;
import com.sharp.vn.its.management.entity.SystemEntity;
import com.sharp.vn.its.management.entity.TaskEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.exception.ITSException;
import com.sharp.vn.its.management.exception.ObjectNotFoundException;
import com.sharp.vn.its.management.filter.CriteriaFilterItem;
import com.sharp.vn.its.management.filter.CriteriaSearchRequest;
import com.sharp.vn.its.management.filter.SortCriteria;
import com.sharp.vn.its.management.repositories.SystemRepository;
import com.sharp.vn.its.management.repositories.TaskRepository;
import com.sharp.vn.its.management.repositories.UserRepository;
import com.sharp.vn.its.management.util.CollectionUtils;
import com.sharp.vn.its.management.util.DateTimeUtil;
import com.sharp.vn.its.management.util.ExcelUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
     * The constant HEADERS.
     */
    private static final List<String> HEADERS = Arrays.asList(
            "依頼番号", "担当者", "受付日", "作業期限", "作業開始日", "作業終了日",
            "作業内容", "状況", "工数", "システム環境", "タイプ", "Ticket 番号",
            "Ticket URL", "備考"
    );
    /**
     * The constant SHEET_NAME.
     */
    private static final String SHEET_NAME = "Tasks";


    /**
     * Gets all tasks.
     *
     * @param request the request
     * @return the all tasks
     */
    public Page<TaskDTO> getAllTasks(TaskDTO request) {
        log.info("Fetching all tasks...");
        CriteriaSearchRequest filter = request.getFilter();
        Map<String, CriteriaFilterItem> searchParam = filter.getSearchParam();
        Specification<TaskEntity> specification = buildFilterCondition(filter);
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
            log.error("Task id empty or null");
            throw new DataValidationException(MessageCode.ERROR_TASK_ID_NOT_FOUND);
        }
        log.info("Fetching task detail for id: {}", id);
        final TaskDTO taskDTO = new TaskDTO(taskRepository.findById(id).orElseThrow(() -> {
            log.error("Task not found with id: {}", id);
            return new ObjectNotFoundException(MessageCode.ERROR_TASK_NOT_FOUND);
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
            log.error("Task id not found");
            throw new DataValidationException(MessageCode.ERROR_TASK_ID_NOT_FOUND);
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
            taskEntity = taskRepository.findById(taskId).orElseThrow(() -> {
                        log.error("Task not found with id: {}", taskId);
                        return new DataValidationException(MessageCode.ERROR_TASK_ID_NOT_FOUND);
                    });
        }
        final String userName = authenticationService.getUser().getUsername();
        if (userName == null) {
            throw new ObjectNotFoundException("User not found");
        }
        final SystemEntity system = systemRepository.findById(systemId).orElseThrow(
                () -> {
                    log.error("System not found with id: {}", systemId);
                    return new ObjectNotFoundException(MessageCode.ERROR_SYSTEM_ID_NOT_FOUND);
                });

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

    /**
     * Load task data byte [ ].
     *
     * @param request the request
     * @return the byte [ ]
     */
    public byte[] loadTaskData(TaskDTO request) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Sheet sheet = ExcelUtils.createSheet(workbook, SHEET_NAME);
            ExcelUtils.addHeaderRow(sheet, HEADERS);
            CellStyle headerStyle = ExcelUtils.createHeaderStyle(workbook);
            Row headerRow = sheet.getRow(0);
            headerRow.forEach(cell -> ExcelUtils.formatCell(cell, headerStyle));
            CriteriaSearchRequest filter = request.getFilter();
            List<TaskEntity> tasks = taskRepository.findAll(buildFilterCondition(filter));
            List<TaskDTO> data = tasks.stream().map(TaskDTO::new).toList();
            for (int i = 0; i < data.size(); i++) {
                TaskDTO task = data.get(i);
                Row row = sheet.createRow(i + 1);
                ExcelUtils.writeCell(row, 0, task.getTaskId());
                ExcelUtils.writeCell(row, 1, task.getFirstName());
                ExcelUtils.writeCell(row, 2, DateTimeUtil.toLocalDateTime(task.getReceiveDate()));
                ExcelUtils.writeCell(row, 3, DateTimeUtil.toLocalDateTime(task.getExpiredDate()));
                ExcelUtils.writeCell(row, 4, DateTimeUtil.toLocalDateTime(task.getStartDate()));
                ExcelUtils.writeCell(row, 5, DateTimeUtil.toLocalDateTime(task.getEndDate()));
                ExcelUtils.writeCell(row, 6, task.getContent());
                TaskStatus taskStatus = TaskStatus.valueOf(task.getStatus());
                if (taskStatus != null) {
                    ExcelUtils.writeCell(row, 7, TaskStatus.valueOf(taskStatus.getStatus()).getDescription());
                }

                ExcelUtils.writeCell(row, 8, task.getCost());
                ExcelUtils.writeCell(row, 9, task.getSystemName());
                TaskType taskType = TaskType.valueOf(task.getType());
                if (taskType != null) {
                    ExcelUtils.writeCell(row, 10, TaskType.valueOf(taskType.getType()).getDescription());
                }
                ExcelUtils.writeCell(row, 11, task.getTicketNumber());
                ExcelUtils.writeCell(row, 12, task.getTicketURL());
                ExcelUtils.writeCell(row, 13, task.getNote());
            }
            workbook.write(bos);
            return bos.toByteArray();

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ITSException("Failed to load task data", e);
        }
    }




    /**
     * Build filter condition specification.
     *
     * @param filter the filter
     * @return the specification
     */
    private Specification<TaskEntity> buildFilterCondition(
            CriteriaSearchRequest filter) {
        Map<String, CriteriaFilterItem> searchParam = filter.getSearchParam();
        Map<String, SortCriteria> sort = filter.getSort();
        buildSortCondition(sort);
        return
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

    }

    /**
     * Duplicate task.
     *
     * @param taskId        the taskId
     * @param numberOfTasks the number of tasks
     */
    public void cloneTask(Long taskId, int numberOfTasks){
        if (taskId == null) {
            log.error("Task id not found");
            throw new DataValidationException(MessageCode.ERROR_TASK_ID_NOT_FOUND);
        }
        log.info("Start cloning task: {}", taskId);
        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("Task not found with id: {}", taskId);
            return new ObjectNotFoundException(MessageCode.ERROR_TASK_ID_NOT_FOUND);
        });
        List<TaskEntity> taskEntities = new ArrayList<>();
        for (int i = 0 ; i < numberOfTasks; i++){
            TaskEntity taskClone = new TaskEntity();
            BeanUtils.copyProperties(taskEntity, taskClone);
            taskClone.setId(null);
            taskEntities.add(taskClone);
        }
        taskRepository.saveAll(taskEntities);
        log.info("Clone {} tasks successfully.", numberOfTasks);
    }

    /**
     * Build sort condition.
     *
     * @param sort the sort
     */
    private void buildSortCondition(Map<String, SortCriteria> sort) {
        if (sort.isEmpty()) {
            sort.put("updatedDate", new SortCriteria("updatedDate", SortType.DESC.getText()));
            return;
        }
        sort.forEach((key, criteria) -> {
            switch (key) {
                case "taskId":
                    criteria.setFieldName("id");
                    break;
                case "systemName":
                    criteria.setFieldName("system.systemName");
                    break;
                case "fullName":
                    criteria.setFieldName("personInCharge.fullName");
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * Gets task counts by status and system.
     *
     * @return the task counts by status and system
     */
    public List<Object[]> getTaskCountsBySystemAndStatus(List<Integer> systemIds, List<Integer> years) {
        List<Long> systemIdsLong = systemIds == null ? null : systemIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        return taskRepository.countTasksBySystemAndStatusAndYear(systemIdsLong, years);
    }
private int getCurrentYear()
{
    LocalDate currentDate = LocalDate.now();
    return currentDate.getYear();
}
    /**
     * Count tasks by system name and status list.
     *
     * @return the list
     */
    public TotalItem countTasksBySystemAndStatusAndYear(List<Long> systemIds, List<Integer> years) {
        if (systemIds.isEmpty()) {
            systemIds = taskRepository.findAllSystemId();
        }
        if (years.isEmpty()) {
            years.add(getCurrentYear());
        }

        List<Object[]> results = taskRepository.countTasksBySystemAndStatusAndYear(systemIds, years);
        Map<String, ChartDTO> chartDataMap = new HashMap<>();
        Map<Integer, Integer> totalValueMap = new HashMap<>();
        int totalCount = 0;

        for (Object[] row : results) {
            String systemName = (String) row[0];
            Integer statusId = ((Number) row[1]).intValue();
            Integer count = ((Number) row[2]).intValue();

            ChartDTO chartData = chartDataMap.getOrDefault(systemName, new ChartDTO(null, systemName, new HashMap<>(), 0));
            chartData.getValue().put(statusId, count);
            chartData.setTotalCount(chartData.getTotalCount() + count);
            chartDataMap.put(systemName, chartData);

            totalValueMap.put(statusId, totalValueMap.getOrDefault(statusId, 0) + count);
            totalCount += count;
        }

        List<ChartDTO> chartDataList = new ArrayList<>(chartDataMap.values());
        TotalDTO totalDTO = new TotalDTO(totalValueMap, totalCount);

        TotalItem totalItem = new TotalItem();
        totalItem.setData(chartDataList);
        totalItem.setTotal(totalDTO);

        return totalItem;
    }

}


