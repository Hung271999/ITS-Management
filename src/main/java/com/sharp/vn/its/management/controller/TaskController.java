package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.TaskDto;
import com.sharp.vn.its.management.dto.TaskInforDto;
import com.sharp.vn.its.management.entity.TaskEntity;
import com.sharp.vn.its.management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/info")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public TaskInforDto loadTaskInformation() {
        return taskService.loadTaskInformation();
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public TaskDto saveTask(@RequestBody TaskDto request) {
        return taskService.saveTask(request);
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<TaskDto> loadTask() {
        return taskService.loadAllTask();
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public TaskDto updateTask(@PathVariable Long taskId, @RequestBody TaskDto request) {
        return taskService.saveTask(request);
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public TaskDto getTaskDetail(@PathVariable Long taskId) {
        return taskService.getTaskDetail(taskId);
    }


}
