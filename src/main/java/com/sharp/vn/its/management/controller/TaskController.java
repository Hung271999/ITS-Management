package com.sharp.vn.its.management.controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.sharp.vn.its.management.dto.task.TaskDTO;
import com.sharp.vn.its.management.entity.TaskEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.repositories.TaskRepository;
import com.sharp.vn.its.management.service.TaskService;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The type Task controller.
 */
@RestController
@RequestMapping(value = "/tasks")
public class TaskController extends BaseController {

    /**
     * The Task service.
     */
    @Autowired
    private TaskService taskService;

    /**
     * Save task task dto.
     *
     * @param request the request
     * @return the task dto
     */
    @PostMapping
    public TaskDTO saveTask(@Valid @RequestBody TaskDTO request) {
        return taskService.saveTask(request);
    }

    /**
     * Delete task response entity.
     *
     * @param taskId the task id
     * @return the response entity
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable(required = true) Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    /**
     * Update task task dto.
     *
     * @param taskId  the task id
     * @param request the request
     * @return the task dto
     */
    @PutMapping("/{taskId}")
    public TaskDTO updateTask(@PathVariable(required = true) Long taskId,
                              @RequestBody TaskDTO request) {
        return taskService.saveTask(request);
    }

    /**
     * Gets task detail.
     *
     * @param taskId the task id
     * @return the task detail
     */
    @GetMapping("/{taskId}")
    public TaskDTO getTaskDetail(@PathVariable(required = true) Long taskId) {
        return taskService.getTaskDetail(taskId);
    }

    /**
     * Load all tasks list.
     *
     * @return the list
     */
    @PostMapping("/all")
    public Page<TaskDTO> loadAllTasks(@RequestBody TaskDTO request) {
        return taskService.getAllTasks(request);
    }

    @PostMapping("export")
    public ResponseEntity exportTaskData(@RequestBody TaskDTO request) {
        byte[] data = taskService.loadTaskData(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "[ITS -VN] Task.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(data);
    }

    /**
     * Upload file.
     *
     * @param file the multipart file (.csv)
     */
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/upload-excel")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException, CsvException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please to a file to upload!", HttpStatus.BAD_REQUEST);
        }
        taskService.uploadFile(file);
        return new ResponseEntity<>("Uploaded and processed CSV file successfully!", HttpStatus.OK);
    }
}
