package org.burgas.entitygraph.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burgas.entitygraph.dto.DepartmentRequest;
import org.burgas.entitygraph.dto.DepartmentResponse;
import org.burgas.entitygraph.entity.Department;
import org.burgas.entitygraph.entity.Employee;
import org.burgas.entitygraph.exception.DepartmentNotFoundException;
import org.burgas.entitygraph.exception.EmployeeNotFoundException;
import org.burgas.entitygraph.mapper.DepartmentMapper;
import org.burgas.entitygraph.repository.DepartmentRepository;
import org.burgas.entitygraph.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.burgas.entitygraph.message.DepartmentMessages.*;
import static org.burgas.entitygraph.message.EmployeeMessages.EMPLOYEE_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = SUPPORTS)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final EmployeeRepository employeeRepository;

    public List<DepartmentResponse> findAll() {
        return this.departmentRepository.findAll()
                .parallelStream()
                .map(this.departmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public DepartmentResponse findById(Long departmentId) {
        return this.departmentRepository.findById(departmentId == null ? 0L : departmentId)
                .map(this.departmentMapper::toResponse)
                .orElseThrow(
                        () -> new DepartmentNotFoundException(DEPARTMENT_NOT_FOUND.getMessage())
                );
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public DepartmentResponse createOrUpdate(DepartmentRequest departmentRequest) {
        return this.departmentMapper.toResponse(
                this.departmentRepository.save(this.departmentMapper.toEntity(departmentRequest))
        );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String addEmployeeToDepartment(final Long departmentId, final Long employeeId) {
        return this.departmentRepository.findById(departmentId == null ? 0L : departmentId)
                .map(
                        department -> this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                                .map(
                                        employee -> {
                                            department.addEmployee(employee);
                                            return EMPLOYEE_ADDED_TO_DEPARTMENT.getMessage();
                                        }
                                )
                                .orElseThrow(() -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage()))
                )
                .orElseThrow(() -> new DepartmentNotFoundException(DEPARTMENT_NOT_FOUND.getMessage()));
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String removeEmployeeFromDepartment(final Long departmentId, final Long employeeId) {
        Department department = this.departmentRepository.findById(departmentId == null ? 0L : departmentId)
                .orElseThrow(
                        () -> new DepartmentNotFoundException(DEPARTMENT_NOT_FOUND.getMessage())
                );
        Employee employee = this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                .orElseThrow(
                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                );
        department.removeEmployee(employee);
        return EMPLOYEE_REMOVED_FROM_DEPARTMENT.getMessage();
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = SUPPORTS,
            rollbackFor = Exception.class
    )
    public String deleteById(final Long departmentId) {
        return this.departmentRepository.findById(departmentId == null ? 0L : departmentId)
                .map(
                        department -> {
                            this.departmentRepository.deleteById(department.getId());
                            return DEPARTMENT_DELETED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new DepartmentNotFoundException(DEPARTMENT_NOT_FOUND.getMessage())
                );
    }
}
