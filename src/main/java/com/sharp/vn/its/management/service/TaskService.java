package com.sharp.vn.its.management.service;


import ch.qos.logback.core.boolex.EvaluationException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.sharp.vn.its.management.constants.FilterType;
import com.sharp.vn.its.management.constants.SortType;
import com.sharp.vn.its.management.constants.TaskStatus;
import com.sharp.vn.its.management.constants.TaskType;
import com.sharp.vn.its.management.dto.task.TaskDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
     * @return the all tasks
     */
    public Page<TaskDTO> getAllTasks(TaskDTO request) {
        log.info("Fetching all tasks...");
        CriteriaSearchRequest filter = request.getFilter();
        Map<String, CriteriaFilterItem> searchParam = filter.getSearchParam();
        Map<String, SortCriteria> sort = new HashMap<>();
        sort.put("updatedDate", new SortCriteria("updatedDate", SortType.DESC.getText()));
        filter.setSort(sort);
        Specification<TaskEntity> specification = buildFilterCondition(searchParam);
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
            Map<String, CriteriaFilterItem> searchParam = filter.getSearchParam();
            List<TaskEntity> tasks = taskRepository.findAll(buildFilterCondition(searchParam));
            List<TaskDTO> data = tasks.stream().map(TaskDTO::new).toList();
            for (int i = 0; i < data.size(); i++) {
                TaskDTO task = data.get(i);
                Row row = sheet.createRow(i + 1);
                ExcelUtils.writeCell(row, 0, task.getTaskId());
                ExcelUtils.writeCell(row, 1, task.getFullName());
                ExcelUtils.writeCell(row, 2, DateTimeUtil.toLocalDateTime(task.getReceiveDate()));
                ExcelUtils.writeCell(row, 3, DateTimeUtil.toLocalDateTime(task.getExpiredDate()));
                ExcelUtils.writeCell(row, 4, DateTimeUtil.toLocalDateTime(task.getStartDate()));
                ExcelUtils.writeCell(row, 5, DateTimeUtil.toLocalDateTime(task.getEndDate()));
                ExcelUtils.writeCell(row, 6, task.getContent());
                TaskStatus taskStatus = TaskStatus.valueOf(task.getStatus());
                if (taskStatus != null) {
                    ExcelUtils.writeCell(row, 7, taskStatus.getStatus());
                }

                ExcelUtils.writeCell(row, 8, task.getCost());
                ExcelUtils.writeCell(row, 9, task.getSystemName());
                TaskType taskType = TaskType.valueOf(task.getType());
                if (taskType != null) {
                    ExcelUtils.writeCell(row, 10, taskType.getType());
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
     * @param searchParam the search param
     * @return the specification
     */
    private Specification<TaskEntity> buildFilterCondition(
            Map<String, CriteriaFilterItem> searchParam) {
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

    public void uploadFile(MultipartFile file) throws IOException, CsvException {
        InputStream inputStream = file.getInputStream();
        Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> rows = csvReader.readAll();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        rows.remove(0);
        for (String[] row : rows) {
            TaskEntity task = new TaskEntity();
//            task.setId(Long.parseLong(row[0]));
            task.setPersonInCharge(userRepository.findById(Long.parseLong(row[1])).orElseThrow());
            try {
                task.setReceiveDate(DateTimeUtil.toLocalDateTime(row[2], dateTimeFormatter));
                task.setExpiredDate(DateTimeUtil.toLocalDateTime(row[3], dateTimeFormatter));
                task.setStartDate(DateTimeUtil.toLocalDateTime(row[4], dateTimeFormatter));
                task.setEndDate(DateTimeUtil.toLocalDateTime(row[5], dateTimeFormatter));
                task.setContent(row[6]);
//                Optional.ofNullable(row[9]).orElse("");
                task.setStatus(Integer.parseInt(row[7]));
                if(!row[9].isEmpty())
                    task.setCost(Double.parseDouble(row[9]));
                task.setSystem(systemRepository.findById(Long.parseLong(row[10])).orElseThrow());
                task.setType(Integer.parseInt(row[11]));
                task.setTicketNumber(row[12]);
                task.setTicketURL(row[13]);
                task.setNote(row[14]);
                task.setCreatedBy(userRepository.findById(Long.parseLong(row[15])).orElseThrow());
                task.setUpdatedBy(userRepository.findById(Long.parseLong(row[16])).orElseThrow());
                task.setCreatedDate(LocalDateTime.parse(row[17], dateTimeFormatter));
                task.setUpdatedDate(LocalDateTime.parse(row[18], dateTimeFormatter));
                taskRepository.save(task);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        csvReader.close();
    }
}
