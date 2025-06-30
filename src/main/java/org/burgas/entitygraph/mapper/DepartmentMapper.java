package org.burgas.entitygraph.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.entitygraph.dto.DepartmentRequest;
import org.burgas.entitygraph.dto.DepartmentResponse;
import org.burgas.entitygraph.entity.Department;
import org.burgas.entitygraph.repository.DepartmentRepository;
import org.springframework.stereotype.Component;

import static org.burgas.entitygraph.message.DepartmentMessages.DEPARTMENT_FIELD_DESCRIPTION_EMPTY;
import static org.burgas.entitygraph.message.DepartmentMessages.DEPARTMENT_FIELD_NAME_EMPTY;

@Component
@RequiredArgsConstructor
public final class DepartmentMapper implements EntityMapper<DepartmentRequest, Department, DepartmentResponse> {

    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public Department toEntity(DepartmentRequest departmentRequest) {
        Long departmentId = this.handleData(departmentRequest.getId(), 0L);
        return this.departmentRepository.findById(departmentId)
                .map(
                        department -> Department.builder()
                                .id(department.getId())
                                .name(this.handleData(departmentRequest.getName(), department.getName()))
                                .description(this.handleData(departmentRequest.getDescription(), department.getDescription()))
                                .build()
                )
                .orElseGet(
                        () -> Department.builder()
                                .name(this.handleDataThrowable(departmentRequest.getName(), DEPARTMENT_FIELD_NAME_EMPTY.getMessage()))
                                .description(this.handleDataThrowable(departmentRequest.getDescription(), DEPARTMENT_FIELD_DESCRIPTION_EMPTY.getMessage()))
                                .build()
                );
    }

    @Override
    public DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .employees(
                        department.getEmployees() == null ? null : department.getEmployees().parallelStream()
                                .map(this.employeeMapper::toResponse)
                                .toList()
                )
                .build();
    }
}
