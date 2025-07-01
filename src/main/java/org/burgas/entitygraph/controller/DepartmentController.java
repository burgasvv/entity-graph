package org.burgas.entitygraph.controller;

import lombok.RequiredArgsConstructor;
import org.burgas.entitygraph.dto.DepartmentRequest;
import org.burgas.entitygraph.dto.DepartmentResponse;
import org.burgas.entitygraph.service.DepartmentService;
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
@RequestMapping(value = "/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.departmentService.findAll());
    }

    @GetMapping(value = "/all-by-manager")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartmentsByManager() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.departmentService.findAllByManager());
    }

    @GetMapping(value = "/by-id")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@RequestParam Long departmentId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.departmentService.findById(departmentId));
    }

    @PostMapping(value = "/create-update")
    public ResponseEntity<DepartmentResponse> createOrUpdateDepartment(@RequestBody DepartmentRequest departmentRequest) {
        DepartmentResponse departmentResponse = this.departmentService.createOrUpdate(departmentRequest);
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/departments/by-id?departmentId=" + departmentResponse.getId()))
                .body(departmentResponse);
    }

    @PutMapping(value = "/add-employee")
    public ResponseEntity<String> addEmployeeToDepartment(@RequestParam Long departmentId, @RequestParam Long employeeId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.departmentService.addEmployeeToDepartment(departmentId, employeeId));
    }

    @PutMapping(value = "/remove-employee")
    public ResponseEntity<String> removeEmployeeFromDepartment(@RequestParam Long departmentId, @RequestParam Long employeeId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.departmentService.removeEmployeeFromDepartment(departmentId, employeeId));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteDepartment(@RequestParam Long departmentId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.departmentService.deleteById(departmentId));
    }
}
