package org.burgas.entitygraph.service;

import lombok.RequiredArgsConstructor;
import org.burgas.entitygraph.dto.EmployeeRequest;
import org.burgas.entitygraph.dto.EmployeeResponse;
import org.burgas.entitygraph.entity.Employee;
import org.burgas.entitygraph.entity.Task;
import org.burgas.entitygraph.exception.EmployeeNotFoundException;
import org.burgas.entitygraph.exception.TaskListIsEmptyException;
import org.burgas.entitygraph.exception.TaskNotFoundException;
import org.burgas.entitygraph.mapper.EmployeeMapper;
import org.burgas.entitygraph.repository.EmployeeRepository;
import org.burgas.entitygraph.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.burgas.entitygraph.message.EmployeeMessages.*;
import static org.burgas.entitygraph.message.TaskMessages.TASK_NOT_FOUND_EXCEPTION;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = SUPPORTS)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final TaskRepository taskRepository;

    public List<EmployeeResponse> findAll() {
        return this.employeeRepository.findAll()
                .parallelStream()
                .map(this.employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public EmployeeResponse findById(final Long employeeId) {
        return this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                .map(this.employeeMapper::toResponse)
                .orElseThrow(
                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                );
    }

    public List<EmployeeResponse> findByTaskId(final Long taskId) {
        return this.employeeRepository.findEmployeesByTaskId(taskId == null ? 0L : taskId)
                .parallelStream()
                .map(this.employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public EmployeeResponse createOrUpdate(final EmployeeRequest employeeRequest) {
        return this.employeeMapper.toResponse(
                this.employeeRepository.save(this.employeeMapper.toEntity(employeeRequest))
        );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String addTaskForEmployee(final Long employeeId, final Long taskId) {
        return this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                .map(
                        employee -> this.taskRepository.findById(taskId == null ? 0L : taskId)
                                .map(
                                        task -> {
                                            employee.addTask(task);
                                            return TASK_ADDED_FOR_EMPLOYEE.getMessage();
                                        }
                                )
                                .orElseThrow(
                                        () -> new TaskNotFoundException(TASK_NOT_FOUND_EXCEPTION.getMessage())
                                )
                )
                .orElseThrow(
                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String removeTaskForEmployee(final Long employeeId, final Long taskId) {
        Employee employee = this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                .orElseThrow(
                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                );
        Task task = this.taskRepository.findById(taskId == null ? 0L : taskId)
                .orElseThrow(
                        () -> new TaskNotFoundException(TASK_NOT_FOUND_EXCEPTION.getMessage())
                );
        employee.removeTask(task);
        return TASK_REMOVED_FOR_EMPLOYEE.getMessage();
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String addTaskListForEmployee(final Long employeeId, List<Long> taskIds) {
        if (taskIds == null || taskIds.isEmpty())
            throw new TaskListIsEmptyException(TASK_LIST_EMPTY.getMessage());

        Employee employee = this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                .orElseThrow(
                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                );
        List<Task> tasks = this.taskRepository.findAllById(taskIds);
        employee.addTasks(tasks);
        return TASK_LIST_ADDED_FOR_EMPLOYEE.getMessage();
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String removeTaskListForEmployee(final Long employeeId, List<Long> taskIds) {
        if (taskIds == null || taskIds.isEmpty())
            throw new TaskListIsEmptyException(TASK_LIST_EMPTY.getMessage());

        return this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                .map(
                        employee -> {
                            employee.removeTasks(this.taskRepository.findAllById(taskIds));
                            return TASK_LIST_REMOVED_FOR_EMPLOYEE.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                );
    }
}
