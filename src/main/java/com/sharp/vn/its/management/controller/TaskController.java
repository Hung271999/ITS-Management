package com.sharp.vn.its.management.controller;


import com.sharp.vn.its.management.dto.task.*;

import com.sharp.vn.its.management.dto.task.RequestCloneTaskDTO;
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
    @PostMapping("/clone")
    public ResponseEntity<?> cloneTask(@RequestBody RequestCloneTaskDTO request){
        taskService.cloneTask(request.getTaskId(), request.getNumberOfCloneTask());
        return ResponseEntity.ok().build();
    }

    /**
     * Get all years list.
     *
     * @return the list
     */
    @GetMapping("/all-year")
    public List<Integer> getAllYearsFromExpiredDate(){
        return taskService.getAllYearsFromExpiredDate();
    }

    /**
     * Gets all weeks from expired date.
     *
     * @return the all weeks from expired date
     */
    @GetMapping("/all-week")
    public List<Integer> getAllWeeksFromExpiredDate() {
        return taskService.getAllWeeksFromExpiredDate();
    }

    /**
     * Load users group by name and status chart dto.
     *
     * @param filter the filter
     * @return the chart dto
     */
    @PostMapping("/statistics-by-person-in-charge")
    public TaskDataDTO loadTaskStatisticsByPersonInCharge(@RequestBody TaskFilter filter){
        return taskService.getTaskByPersonInCharge(filter);
    }

    /**
     * Load task statistics by system task data dto.
     *
     * @param filter the filter
     * @return the task data dto
     */
    @PostMapping("/statistics-by-system")
    public TaskDataDTO loadTaskStatisticsBySystem(@RequestBody TaskFilter filter) {
        return taskService.getTaskBySystem(filter);
    }

    /**
     * Load task for system by week task data dto.
     *
     * @param filter the filter
     * @return the task data dto
     */
    @PostMapping("/statistics-by-system-per-week")
    public TaskDataDTO loadTaskForSystemByWeek(@RequestBody TaskFilter filter) {
        return taskService.getTaskSystemByWeek(filter);
    }

    /**
     * Load task for person in charge by week task data dto.
     *
     * @param filter the filter
     * @return the task data dto
     */
    @PostMapping("/statistics-by-person-in-charge-per-week")
    public TaskDataDTO loadTaskByPersonInChargePerWeek(@RequestBody TaskFilter filter){
        return taskService.getTaskByPersonInChargePerWeek(filter);
    }

    /**
     * Load effort by person in charge per week task data dto.
     *
     * @param filter the filter
     * @return the task data dto
     */
    @PostMapping("/statistics-effort-by-person-in-charge-per-week")
    public TaskDataDTO loadEffortByPersonInChargePerWeek(@RequestBody TaskFilter filter){
        return taskService.getEffortByPersonInChargePerWeek(filter);
    }

    /**
     * Load task by group per week task data dto.
     *
     * @param filter the filter
     * @return the task data dto
     */
    @PostMapping("/statistics-task-by-group-per-week")
    public TaskDataDTO loadTaskByGroupPerWeek(@RequestBody TaskFilter filter){
        return taskService.getTaskByGroupPerWeek(filter);
    }

    /**
     * Load task by week for person in charge task data dto.
     *
     * @param filter the filter
     * @return the task data dto
     */
    @PostMapping("/statistics-by-week-for-person-in-charge")
    public TaskDataDTO loadTaskByWeekForPersonInCharge(@RequestBody TaskFilter filter){
        return taskService.getTaskByWeekForPersonInCharge(filter);
    }

    /**
     * Gets effort of system by week.
     *
     * @param filter the filter
     * @return the effort of system by week
     */
    @PostMapping("/statistic-effort-by-system-per-week")
    public TaskDataDTO getEffortOfSystemByWeek(@RequestBody TaskFilter filter) {
        return taskService.findEffortOfSystemByWeek(filter);
    }

    /**
     * Load effort by group per week task data dto.
     *
     * @param filter the filter
     * @return the task data dto
     */
    @PostMapping("/statistics-effort-by-group-per-week")
    public TaskDataDTO loadEffortByGroupPerWeek(@RequestBody TaskFilter filter){
        return taskService.getEffortByGroupPerWeek(filter);
    }

    /**
     * Load all support tasks page.
     *
     * @param request the request
     * @return the page
     */
    @PostMapping("support-effort/all")
    public Page<SupportEffortDTO> loadAllSupportTasks(@RequestBody SupportEffortDTO request) {
        return taskService.getAllSupportTask(request);
    }

    /**
     * Gets support task detail.
     *
     * @param id the id
     * @return the support task detail
     */
    @GetMapping("support-effort/{id}")
    public SupportEffortDTO getSupportTaskDetail(@PathVariable(required = true) Long id) {
        return taskService.getSupportTaskDetail(id);
    }

    /**
     * Delete support task response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("support-effort/{id}")
    public ResponseEntity<?> deleteSupportTask(@PathVariable(required = true) Long id) {
        taskService.deleteSupportTask(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Save support task support effort dto.
     *
     * @param request the request
     * @return the support effort dto
     */
    @PostMapping("support-effort")
    public SupportEffortDTO saveSupportTask(@Valid @RequestBody SupportEffortDTO request) {
        return taskService.saveSupportTask(request);
    }

    /**
     * Update support task support effort dto.
     *
     * @param id      the id
     * @param request the request
     * @return the support effort dto
     */
    @PutMapping("support-effort/{id}")
    public SupportEffortDTO updateSupportTask(@PathVariable(required = true) Long id,
                              @RequestBody SupportEffortDTO request) {
        return taskService.saveSupportTask(request);
    }

    /**
     * Clone support task response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/support-effort/clone")
    public ResponseEntity<?> cloneSupportTask(@RequestBody RequestCloneTaskDTO request){
        taskService.cloneSupportTask(request.getTaskId(), request.getNumberOfCloneTask());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/statistics-effort-by-week-for-ams")
    public TaskDataDTO loadEffortByWeekForAMS(@RequestBody TaskFilter filter){
        return taskService.getEffortByWeekForAMS(filter);
    }
}
