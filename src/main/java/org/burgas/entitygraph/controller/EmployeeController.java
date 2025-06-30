package org.burgas.entitygraph.controller;

import lombok.RequiredArgsConstructor;
import org.burgas.entitygraph.dto.EmployeeRequest;
import org.burgas.entitygraph.dto.EmployeeResponse;
import org.burgas.entitygraph.service.EmployeeService;
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
@RequestMapping(value = "/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.employeeService.findAll());
    }

    @GetMapping(value = "/by-id")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@RequestParam Long employeeId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.employeeService.findById(employeeId));
    }

    @GetMapping(value = "/by-task")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByTask(@RequestParam Long taskId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.employeeService.findByTaskId(taskId));
    }

    @PostMapping(value = "/create-update")
    public ResponseEntity<EmployeeResponse> createOrUpdateEmployee(@RequestBody EmployeeRequest employeeRequest) {
        EmployeeResponse employeeResponse = this.employeeService.createOrUpdate(employeeRequest);
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/employees/by-id?employeeId=" + employeeResponse.getId()))
                .body(employeeResponse);
    }

    @PutMapping(value = "/add-task")
    public ResponseEntity<String> addTaskForEmployee(@RequestParam Long employeeId, @RequestParam Long taskId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.employeeService.addTaskForEmployee(employeeId, taskId));
    }

    @PutMapping(value = "/remove-task")
    public ResponseEntity<String> removeTaskForEmployee(@RequestParam Long employeeId, @RequestParam Long taskId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.employeeService.removeTaskForEmployee(employeeId, taskId));
    }

    @PutMapping(value = "/add-task-list")
    public ResponseEntity<String> addTaskListForEmployee(@RequestParam Long employeeId, @RequestParam List<Long> taskIds) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.employeeService.addTaskListForEmployee(employeeId, taskIds));
    }

    @PutMapping(value = "/remove-task-list")
    public ResponseEntity<String> removeTaskListForEmployee(@RequestParam Long employeeId, @RequestParam List<Long> taskIds) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.employeeService.removeTaskListForEmployee(employeeId, taskIds));
    }
}
