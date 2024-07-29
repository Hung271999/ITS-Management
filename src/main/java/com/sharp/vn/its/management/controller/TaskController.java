package com.sharp.vn.its.management.controller;


import com.sharp.vn.its.management.dto.task.TaskFilter;
import com.sharp.vn.its.management.dto.task.TaskDataDTO;
import com.sharp.vn.its.management.dto.task.RequestCloneTaskDTO;
import com.sharp.vn.its.management.dto.task.TaskDTO;
import com.sharp.vn.its.management.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param request the request
     * @return the list
     */
    @PostMapping("/all")
    public Page<TaskDTO> loadAllTasks(@RequestBody TaskDTO request) {
        return taskService.getAllTasks(request);
    }

    /**
     * Export task data response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/export")
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
     * Clone task response entity.
     *
     * @param request the duplicate task dto
     * @return the response entity
     */
    @PostMapping("/cloneTask")
    public ResponseEntity<?> cloneTask(@RequestBody RequestCloneTaskDTO request) {
        taskService.cloneTask(request.getTaskId(), request.getNumberOfCloneTask());
        return ResponseEntity.ok().build();
    }


    /**
     * Load task statistics by system task data dto.
     *
     * @param filter the filter
     * @return the task data dto
     */
    @PostMapping("/statisticsBySystem")
    public TaskDataDTO loadTaskStatisticsBySystem(@RequestBody TaskFilter filter) {
        return taskService.getTaskBySystem(filter);
    }

    /**
     * Get all years list.
     *
     * @return the list
     */
    @GetMapping("/allYears")
    public List<Integer> getAllYearsFromExpiredDate() {
        return taskService.getAllYearsFromExpiredDate();
    }

}
