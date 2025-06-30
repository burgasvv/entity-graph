package org.burgas.entitygraph.controller;

import lombok.RequiredArgsConstructor;
import org.burgas.entitygraph.dto.TaskRequest;
import org.burgas.entitygraph.dto.TaskResponse;
import org.burgas.entitygraph.service.TaskService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.taskService.findAll());
    }

    @GetMapping(value = "/by-id")
    public ResponseEntity<TaskResponse> getTaskById(@RequestParam Long taskId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.taskService.findById(taskId));
    }

    @PostMapping(value = "/create-update")
    public ResponseEntity<TaskResponse> createOrUpdateTask(@RequestBody TaskRequest taskRequest) {
        TaskResponse taskResponse = this.taskService.createOrUpdate(taskRequest);
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/tasks/by-id?taskId=" + taskResponse.getId()))
                .body(taskResponse);
    }

    @PutMapping(value = "/complete-task")
    public ResponseEntity<TaskResponse> completeTheTask(@RequestParam Long taskId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.taskService.completeTask(taskId));
    }

    @PutMapping(value = "/add-employee")
    public ResponseEntity<String> addEmployeeToTask(@RequestParam Long taskId, @RequestParam Long employeeId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.taskService.addEmployeeToTask(taskId, employeeId));
    }

    @PutMapping(value = "/remove-employee")
    public ResponseEntity<String> removeEmployeeFromTask(@RequestParam Long taskId, @RequestParam Long employeeId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.taskService.removeEmployeeFromTask(taskId, employeeId));
    }

    @PutMapping(value = "/add-employees")
    public ResponseEntity<String> addEmployeesToTask(@RequestParam Long taskId, @RequestParam List<Long> employeeIds) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.taskService.addEmployeesToTask(taskId, employeeIds));
    }

    @PutMapping(value = "/remove-employees")
    public ResponseEntity<String> removeEmployeesFromTask(@RequestParam Long taskId, @RequestParam List<Long> employeeIds) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.taskService.removeEmployeesFromTask(taskId, employeeIds));
    }
}
