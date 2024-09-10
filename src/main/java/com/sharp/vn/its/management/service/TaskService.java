package com.sharp.vn.its.management.service;


import com.sharp.vn.its.management.constants.*;
import com.sharp.vn.its.management.data.TaskData;
import com.sharp.vn.its.management.dto.task.*;
import com.sharp.vn.its.management.constants.FilterType;
import com.sharp.vn.its.management.constants.SortType;
import com.sharp.vn.its.management.constants.TaskStatus;
import com.sharp.vn.its.management.constants.TaskType;
import com.sharp.vn.its.management.entity.SupportEffortEntity;
import com.sharp.vn.its.management.entity.SystemEntity;
import com.sharp.vn.its.management.entity.TaskEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.exception.ITSException;
import com.sharp.vn.its.management.exception.ObjectNotFoundException;
import com.sharp.vn.its.management.filter.CriteriaFilterItem;
import com.sharp.vn.its.management.filter.CriteriaSearchRequest;
import com.sharp.vn.its.management.filter.SortCriteria;
import com.sharp.vn.its.management.repositories.*;
import com.sharp.vn.its.management.util.CollectionUtils;
import com.sharp.vn.its.management.util.DateTimeUtil;
import com.sharp.vn.its.management.util.ExcelUtils;
import jakarta.persistence.criteria.*;
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
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Autowired
    private SupportEffortRepository supportEffortRepository;
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
     * @param taskId the taskId
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
     * Get all years list.
     *
     * @return the list
     */
    public List<Integer> getAllYearsFromExpiredDate(){
        return taskRepository.findAllYearsFromExpiredDate();
    }

    /**
     * Get all weeks from expired date list.
     *
     * @return the list
     */
    public List<Integer> getAllWeeksFromExpiredDate(){
        return taskRepository.findWeeksFromExpiredDate();
    }

    /**
     * Group by chart id and sum total map.
     *
     * @param data the data
     * @return the map
     */
    public Map<Long, Integer> groupByTaskDataIdAndSumTotal(List<TaskData> data) {
        return data.stream()
                .collect(Collectors.groupingBy(
                        TaskData::getId,
                        Collectors.summingInt(taskData -> taskData.getTotal().intValue())
                ));
    }

    /**
     * Gets users group by name and status.
     *
     * @param filter the filter
     * @return the users group by name and status
     */
    public TaskDataDTO getTaskByPersonInCharge(TaskFilter filter) {
        List<TaskData> data = taskRepository.findTaskByPersonInCharge(filter.getUserIds(), filter.getYears());
        Map<Long, List<TaskData>> mapGroupByUserId = data.stream().collect(Collectors.groupingBy(TaskData::getId));

        Map<Long, Integer> totalCountById = groupByTaskDataIdAndSumTotal(data);
        List<TaskDetailDTO> taskDetailDTOList = new ArrayList<>();
        mapGroupByUserId.forEach((id, taskDataList) -> {
            TaskDetailDTO item = new TaskDetailDTO();
            item.setFirstName(taskDataList.get(0).getFirstName());
            item.setValues(taskDataList.stream()
                    .collect(Collectors.toMap(TaskData::getStatus, TaskData::getTotal)));
            item.setTotalCount(totalCountById.get(id));
            taskDetailDTOList.add(item);
        });

        TaskSummaryDTO taskSummaryDTO = new TaskSummaryDTO(data.stream()
                .collect(Collectors.groupingBy(
                        TaskData::getStatus,
                        Collectors.collectingAndThen(Collectors.summingInt(taskData -> taskData.getTotal().intValue()), total -> total)
                )), taskDetailDTOList.stream()
                .mapToInt(taskDetailDTO -> taskDetailDTO.getTotalCount().intValue())
                .sum());
        return new TaskDataDTO(taskSummaryDTO, taskDetailDTOList);
    }

    /**
     * Gets task by system.
     *
     * @param filter the filter
     * @return the task by system
     */
    public TaskDataDTO getTaskBySystem(TaskFilter filter) {
        List<TaskData> data = taskRepository.findTaskBySystem(filter.getSystemIds(), filter.getYears());
        Map<Long, List<TaskData>> mapGroupBySystemId = data.stream().collect(Collectors.groupingBy(TaskData::getId));

        List<TaskDetailDTO> taskDetailDTOS = new ArrayList<>();
        Map<Long, Integer> totalCountById = groupByTaskDataIdAndSumTotal(data);
        mapGroupBySystemId.forEach((id, taskDataList) -> {
            TaskDetailDTO item = new TaskDetailDTO();
            item.setSystemName(taskDataList.get(0).getSystemName());
            item.setValues(taskDataList.stream()
                    .collect(Collectors.toMap(TaskData::getStatus, TaskData::getTotal)));
            item.setTotalCount(totalCountById.get(id));
            taskDetailDTOS.add(item);
        });

        TaskSummaryDTO taskSummaryDTO = new TaskSummaryDTO(
                data.stream()
                        .collect(Collectors.groupingBy(
                                TaskData::getStatus,
                                Collectors.collectingAndThen(Collectors.summingInt(taskData -> taskData.getTotal().intValue()), total -> total)
                        )), taskDetailDTOS.stream()
                .mapToInt(taskDetailDTO -> taskDetailDTO.getTotalCount().intValue())
                .sum());
        return new TaskDataDTO(taskSummaryDTO,taskDetailDTOS);
    }

    /**
     * Get task system by week task data dto.
     *
     * @return the task data dto
     */
    public TaskDataDTO getTaskSystemByWeek(TaskFilter filter) {
        List<TaskData> data = taskRepository.findTaskSystemByWeek(filter.getSystemIds(),filter.getYears(), filter.getWeeks());
        Map<Long, List<TaskData>> mapGroupBySystemId = data.stream().collect(Collectors.groupingBy(TaskData::getId));

        List<TaskDetailDTO> taskDetailDTOS = new ArrayList<>();
        mapGroupBySystemId.forEach((id, chartDataList) -> {
            TaskDetailDTO item = new TaskDetailDTO();
            item.setSystemName(chartDataList.get(0).getSystemName());
            item.setValues(chartDataList.stream()
                    .collect(Collectors.toMap(TaskData::getWeek, TaskData::getTotal)));
            item.setTotalCount(chartDataList.stream()
                    .mapToInt(taskData -> taskData.getTotal().intValue())
                    .sum());
            taskDetailDTOS.add(item);
        });

        TaskSummaryDTO taskSummaryDTO = new TaskSummaryDTO(
                data.stream()
                        .collect(Collectors.groupingBy(
                                TaskData::getWeek,
                                Collectors.collectingAndThen(Collectors.summingInt(taskData -> taskData.getTotal().intValue()), total -> total)
                        )), taskDetailDTOS.stream()
                .mapToInt(taskDetailDTO -> taskDetailDTO.getTotalCount().intValue())
                .sum());
        return new TaskDataDTO(taskSummaryDTO, taskDetailDTOS);
    }

    /**
     * Get task by person in charge per week task data dto.
     *
     * @param filter the filter
     * @return the task data dto
     */
    public TaskDataDTO getTaskByPersonInChargePerWeek(TaskFilter filter){
        List<TaskData> data = taskRepository.findTaskByPersonInChargePerWeek(filter.getUserIds(), filter.getYears(), filter.getWeeks());
        Map<Long, List<TaskData>> mapGroupByUserId = data.stream().collect(Collectors.groupingBy(TaskData::getId));

        Map<Long, Integer> totalCountById = groupByTaskDataIdAndSumTotal(data);
        List<TaskDetailDTO> taskDataItems = new ArrayList<>();
        mapGroupByUserId.forEach((id, chartDataList) -> {
            TaskDetailDTO item = new TaskDetailDTO();
            item.setFirstName(chartDataList.get(0).getFirstName());
            item.setValues(chartDataList.stream()
                    .collect(Collectors.toMap(TaskData::getWeek, TaskData::getTotal)));
            item.setTotalCount(totalCountById.get(id));
            taskDataItems.add(item);
        });

        TaskSummaryDTO taskSummaryDTO = new TaskSummaryDTO(
                data.stream()
                        .collect(Collectors.groupingBy(
                                TaskData::getWeek,
                                Collectors.collectingAndThen(Collectors.summingInt(taskData -> taskData.getTotal().intValue()), total -> total)
                        )), taskDataItems.stream()
                .mapToInt(taskDetailDTO -> taskDetailDTO.getTotalCount().intValue())
                .sum());
        return new TaskDataDTO(taskSummaryDTO,taskDataItems);
    }

    /**
     * Get effort by person in charge per week task data dto.
     *
     * @param filter the filter
     * @return the task data dto
     */
    public TaskDataDTO getEffortByPersonInChargePerWeek(TaskFilter filter){
        List<TaskData> data = taskRepository.findEffortByPersonInChargePerWeek(filter.getUserIds(), filter.getYears(), filter.getWeeks());
        Map<Integer, List<TaskData>> mapGroupByWeek = data.stream().collect(Collectors.groupingBy(TaskData::getWeek, TreeMap::new, Collectors.toList()));

        Map<Integer, Double> totalCountByWeek = data.stream()
                .collect(Collectors.groupingBy(
                        TaskData::getWeek,
                        Collectors.summingDouble(taskData -> taskData.getTotal().doubleValue())
                ));
        List<TaskDetailDTO> taskDataItems = new ArrayList<>();
        mapGroupByWeek.forEach((week, chartDataList) -> {
            TaskDetailDTO item = new TaskDetailDTO();
            item.setWeek(chartDataList.get(0).getWeek());
            item.setValues(chartDataList.stream()
                    .collect(Collectors.toMap(taskData -> taskData.getId().intValue(), TaskData::getTotal)));
            item.setTotalCount(totalCountByWeek.get(week));
            taskDataItems.add(item);
        });

        TaskSummaryDTO taskSummaryDTO = new TaskSummaryDTO(
                data.stream()
                        .collect(Collectors.groupingBy(
                                taskData -> taskData.getId().intValue(),
                                Collectors.collectingAndThen(Collectors.summingDouble(taskData -> taskData.getTotal().doubleValue()), total -> total)
                        )),
                taskDataItems.stream()
                        .mapToDouble(taskDetailDTO -> taskDetailDTO.getTotalCount().doubleValue())
                        .sum());
        return new TaskDataDTO(taskSummaryDTO,taskDataItems);
    }

    /**
     * Get task by group per week task data dto.
     *
     * @param filter the filter
     * @return the task data dto
     */
    public TaskDataDTO getTaskByGroupPerWeek(TaskFilter filter){
        List<TaskData> data = taskRepository.findTaskByGroupPerWeek(filter.getGroupIds(), filter.getYears(), filter.getWeeks());
        Map<Integer, List<TaskData>> mapGroupByWeek = data.stream().collect(Collectors.groupingBy(TaskData::getWeek, TreeMap::new, Collectors.toList()));

        Map<Integer, Integer> totalCountByWeek = data.stream()
                .collect(Collectors.groupingBy(
                        TaskData::getWeek,
                        Collectors.summingInt(taskData -> taskData.getTotal().intValue())
                ));
        List<TaskDetailDTO> taskDataItems = new ArrayList<>();
        mapGroupByWeek.forEach((week, taskDataList) -> {
            TaskDetailDTO item = new TaskDetailDTO();
            item.setWeek(taskDataList.get(0).getWeek());
            item.setValues(taskDataList.stream()
                    .collect(Collectors.toMap(taskData -> taskData.getId().intValue(), TaskData::getTotal)));
            item.setTotalCount(totalCountByWeek.get(week));
            taskDataItems.add(item);
        });

        TaskSummaryDTO taskSummaryDTO = new TaskSummaryDTO(
                data.stream()
                        .collect(Collectors.groupingBy(
                                taskData -> taskData.getId().intValue(),
                                Collectors.collectingAndThen(Collectors.summingInt(taskData -> taskData.getTotal().intValue()), total -> total)
                        )),
                taskDataItems.stream()
                        .mapToInt(taskDetailDTO -> taskDetailDTO.getTotalCount().intValue())
                        .sum());
        return new TaskDataDTO(taskSummaryDTO,taskDataItems);
    }

    /**
     * Get task by week for person in charge task data dto.
     *
     * @param filter the filter
     * @return the task data dto
     */
    public TaskDataDTO getTaskByWeekForPersonInCharge(TaskFilter filter){
        List<TaskData> data = taskRepository.findTaskByPersonInChargePerWeek(filter.getUserIds(), filter.getYears(), filter.getWeeks());
        Map<Integer, List<TaskData>> mapGroupByWeek = data.stream().collect(Collectors.groupingBy(TaskData::getWeek, TreeMap::new, Collectors.toList()));

        Map<Integer, Integer> totalCountByWeek = data.stream()
                .collect(Collectors.groupingBy(
                        TaskData::getWeek,
                        Collectors.summingInt(taskData -> taskData.getTotal().intValue())
                ));
        List<TaskDetailDTO> taskDataItems = new ArrayList<>();
        mapGroupByWeek.forEach((week, chartDataList) -> {
            TaskDetailDTO item = new TaskDetailDTO();
            item.setWeek(chartDataList.get(0).getWeek());
            item.setValues(chartDataList.stream()
                    .collect(Collectors.toMap(taskData -> taskData.getId().intValue(), TaskData::getTotal)));
            item.setTotalCount(totalCountByWeek.get(week));
            taskDataItems.add(item);
        });

        TaskSummaryDTO taskSummaryDTO = new TaskSummaryDTO(
                data.stream()
                        .collect(Collectors.groupingBy(
                                taskData -> taskData.getId().intValue(),
                                Collectors.collectingAndThen(Collectors.summingDouble(taskData -> taskData.getTotal().doubleValue()), total -> total)
                        )),
                taskDataItems.stream()
                        .mapToDouble(taskDetailDTO -> taskDetailDTO.getTotalCount().doubleValue())
                        .sum());
        return new TaskDataDTO(taskSummaryDTO,taskDataItems);
    }

    /**
     * Build sort condition for support task.
     *
     * @param sort the sort
     */
    private void buildSortConditionForSupportTask(Map<String, SortCriteria> sort) {
        if (sort.isEmpty()) {
            sort.put("updatedDate", new SortCriteria("updatedDate", SortType.DESC.getText()));
            return;
        }
        sort.forEach((key, criteria) -> {
            switch (key) {
                case "supportId":
                    criteria.setFieldName("id");
                    break;
                case "systemName":
                    criteria.setFieldName("system.systemName");
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * Build filter condition for support task specification.
     *
     * @param filter the filter
     * @return the specification
     */
    private Specification<SupportEffortEntity> buildFilterConditionForSupportTask(CriteriaSearchRequest filter) {
        Map<String, CriteriaFilterItem> searchParam = filter.getSearchParam();
        Map<String, SortCriteria> sort = filter.getSort();
        buildSortConditionForSupportTask(sort);
        return
                (Root<SupportEffortEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
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
                                            searchParam.get("content")),
                                    buildPredicate(criteriaBuilder, root,
                                            searchParam.get("participants")),
                                    buildPredicate(criteriaBuilder, root,
                                            searchParam.get("implementer"))));

                    CriteriaFilterItem system = searchParam.get("system");
                    if (system != null) {
                        Join<SupportEffortEntity, SystemEntity> systemJoin = root.join("system");
                        CollectionUtils.addIfNotEmptyOrNull(predicates,
                                criteriaBuilder.equal(systemJoin.get("id"),
                                        system.getFilterNumberValue().getToValue()));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };
    }

    /**
     * Gets all support task.
     *
     * @param request the request
     * @return the all support task
     */
    public Page<SupportEffortDTO> getAllSupportTask(SupportEffortDTO request) {
        log.info("Fetching all support effort tasks...");
        CriteriaSearchRequest filter = request.getFilter();
        Specification<SupportEffortEntity> specification = buildFilterConditionForSupportTask(filter);
        Page<SupportEffortEntity> pageable = supportEffortRepository.findAll(specification, request.getFilter().getPageable());
        log.info("All support effort tasks fetched successfully.");
        return pageable.map(SupportEffortDTO::new);
    }

    /**
     * Gets support task detail.
     *
     * @param id the id
     * @return the support task detail
     */
    public SupportEffortDTO getSupportTaskDetail(Long id) {
        if (id == null) {
            log.error("Support effort task id empty or null");
            throw new DataValidationException(MessageCode.ERROR_SUPPORT_TASK_ID_NOT_FOUND);
        }
        log.info("Fetching support effort task detail for id: {}", id);
        final SupportEffortDTO supportEffortDTO = new SupportEffortDTO(supportEffortRepository.findById(id).orElseThrow(() -> {
            log.error("Support effort task not found with id: {}", id);
            return new ObjectNotFoundException(MessageCode.ERROR_SUPPORT_TASK_NOT_FOUND);
        }));
        log.info("Support effort task detail fetched successfully for id: {}", id);
        return supportEffortDTO;
    }

    /**
     * Delete support task.
     *
     * @param id the id
     */
    public void deleteSupportTask(Long id) {
        if (id == null) {
            log.error("Support effort task id not found");
            throw new DataValidationException(MessageCode.ERROR_SUPPORT_TASK_ID_NOT_FOUND);
        }
        supportEffortRepository.deleteById(id);
        log.info("Support effort task with id {} deleted successfully.", id);
    }

    public SupportEffortDTO saveSupportTask(SupportEffortDTO supportEffortDTO) {
        log.info("Saving support effort task...");
        final Long supportTaskId = supportEffortDTO.getSupportId();
        final Long systemId = supportEffortDTO.getSystem();
        SupportEffortEntity supportEffortEntity = new SupportEffortEntity();

        // update when support effort task id is not null
        if (supportTaskId != null) {
            supportEffortEntity = supportEffortRepository.findById(supportTaskId).orElseThrow(() -> {
                log.error("Support effort task not found with id: {}", supportTaskId);
                return new DataValidationException(MessageCode.ERROR_SUPPORT_TASK_ID_NOT_FOUND);
            });
        }
        final SystemEntity system = systemRepository.findById(systemId).orElseThrow(
                () -> {
                    log.error("System not found with id: {}", systemId);
                    return new ObjectNotFoundException(MessageCode.ERROR_SYSTEM_ID_NOT_FOUND);
                });
        final UserEntity userEntity = userRepository.findById(authenticationService.getUser()
                .getId()).get();
        final SupportEffortType supportEffortType = SupportEffortType.valueOf(supportEffortDTO.getType());
        final SupportEffortStatus supportEffortStatus = SupportEffortStatus.valueOf(supportEffortDTO.getStatus());

        // set properties
        BeanUtils.copyProperties(supportEffortDTO, supportEffortEntity);
        supportEffortEntity.setStartDate(DateTimeUtil.toLocalDateTime(supportEffortDTO.getStartDate()));
        supportEffortEntity.setEndDate(DateTimeUtil.toLocalDateTime(supportEffortDTO.getEndDate()));
        supportEffortEntity.setSystem(system);
        supportEffortEntity.setType(supportEffortType.getType());
        supportEffortEntity.setStatus(supportEffortStatus.getStatus());
        supportEffortEntity.setCreatedBy(userEntity);
        supportEffortEntity.setUpdatedBy(userEntity);
        supportEffortEntity = supportEffortRepository.save(supportEffortEntity);
        log.info("Support effort task saved successfully.");
        return new SupportEffortDTO(supportEffortEntity);
    }

    /**
     * Clone support task.
     *
     * @param supportTaskId the support task id
     * @param numberOfTasks the number of tasks
     */
    public void cloneSupportTask(Long supportTaskId, int numberOfTasks){
        if (supportTaskId == null) {
            log.error("Support effort task id not found");
            throw new DataValidationException(MessageCode.ERROR_SUPPORT_TASK_ID_NOT_FOUND);
        }
        log.info("Start cloning support effort task: {}", supportTaskId);
        SupportEffortEntity supportEffortEntity = supportEffortRepository.findById(supportTaskId).orElseThrow(() -> {
            log.error("Support effort task not found with id: {}", supportTaskId);
            return new ObjectNotFoundException(MessageCode.ERROR_SUPPORT_TASK_ID_NOT_FOUND);
        });
        supportEffortRepository.saveAll(
                IntStream.range(0, numberOfTasks)
                        .mapToObj(i -> {
                            SupportEffortEntity taskClone = new SupportEffortEntity();
                            BeanUtils.copyProperties(supportEffortEntity, taskClone);
                            taskClone.setId(null);
                            return taskClone;
                        })
                        .collect(Collectors.toList())
        );
        log.info("Clone {} support effort tasks successfully.", numberOfTasks);
    }

    /**
     * Get effort by type per week task data dto.
     *
     * @param filter the filter
     * @return the task data dto
     */
    public TaskDataDTO getEffortByTypePerWeek(TaskFilter filter){
        List<TaskData> data = taskRepository.findEffortByWeekForType(filter.getTypes(), filter.getYears(), filter.getWeeks());
        Map<Integer, List<TaskData>> mapGroupByWeek = data.stream().collect(Collectors.groupingBy(TaskData::getWeek, TreeMap::new, Collectors.toList()));

        Map<Integer, Double> totalCountByWeek = data.stream()
                .collect(Collectors.groupingBy(
                        TaskData::getWeek,
                        Collectors.summingDouble(taskData -> taskData.getTotal().doubleValue())
                ));
        List<TaskDetailDTO> taskDataItems = new ArrayList<>();
        mapGroupByWeek.forEach((week, chartDataList) -> {
            TaskDetailDTO item = new TaskDetailDTO();
            item.setWeek(chartDataList.get(0).getWeek());
            item.setValues(chartDataList.stream()
                    .collect(Collectors.toMap(taskData -> taskData.getId().intValue(), TaskData::getTotal)));
            item.setTotalCount(totalCountByWeek.get(week));
            taskDataItems.add(item);
        });

        TaskSummaryDTO taskSummaryDTO = new TaskSummaryDTO(
                data.stream()
                        .collect(Collectors.groupingBy(
                                taskData -> taskData.getId().intValue(),
                                Collectors.collectingAndThen(Collectors.summingDouble(taskData -> taskData.getTotal().doubleValue()), total -> total)
                        )),
                taskDataItems.stream()
                        .mapToDouble(taskDetailDTO -> taskDetailDTO.getTotalCount().doubleValue())
                        .sum());
        return new TaskDataDTO(taskSummaryDTO,taskDataItems);
    }
}
