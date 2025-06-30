package org.burgas.entitygraph.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.entitygraph.dto.EmployeeRequest;
import org.burgas.entitygraph.dto.EmployeeResponse;
import org.burgas.entitygraph.entity.Employee;
import org.burgas.entitygraph.repository.DepartmentRepository;
import org.burgas.entitygraph.repository.EmployeeRepository;
import org.springframework.stereotype.Component;

import static org.burgas.entitygraph.message.EmployeeMessages.*;

@Component
@RequiredArgsConstructor
public final class EmployeeMapper implements EntityMapper<EmployeeRequest, Employee, EmployeeResponse> {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public Employee toEntity(EmployeeRequest employeeRequest) {
        Long employeeId = this.handleData(employeeRequest.getId(), 0L);
        return this.employeeRepository.findById(employeeId)
                .map(
                        employee -> Employee.builder()
                                .id(employee.getId())
                                .name(this.handleData(employeeRequest.getName(), employee.getName()))
                                .surname(this.handleData(employeeRequest.getSurname(), employee.getSurname()))
                                .patronymic(this.handleData(employeeRequest.getPatronymic(), employee.getPatronymic()))
                                .department(
                                        this.handleData(
                                                this.departmentRepository.findById(
                                                        employeeRequest.getDepartmentId() == null ? 0L : employeeRequest.getDepartmentId()
                                                ).orElse(null),
                                                employee.getDepartment()
                                        )
                                )
                                .build()
                )
                .orElseGet(
                        () -> Employee.builder()
                                .name(this.handleDataThrowable(employeeRequest.getName(), EMPLOYEE_FIELD_NAME_EMPTY.getMessage()))
                                .surname(this.handleDataThrowable(employeeRequest.getSurname(), EMPLOYEE_FIELD_SURNAME_EMPTY.getMessage()))
                                .patronymic(this.handleDataThrowable(employeeRequest.getPatronymic(), EMPLOYEE_FIELD_PATRONYMIC_EMPTY.getMessage()))
                                .department(
                                        this.handleDataThrowable(
                                                this.departmentRepository.findById(
                                                        employeeRequest.getDepartmentId() == null ? 0L : employeeRequest.getDepartmentId()
                                                ).orElse(null),
                                                EMPLOYEE_FIELD_DEPARTMENT_EMPTY.getMessage()
                                        )
                                )
                                .build()
                );
    }

    @Override
    public EmployeeResponse toResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .patronymic(employee.getPatronymic())
                .department(employee.getDepartment() == null ? null : employee.getDepartment().getName())
                .build();
    }
}
