package org.burgas.entitygraph.service;

import lombok.RequiredArgsConstructor;
import org.burgas.entitygraph.dto.TaskRequest;
import org.burgas.entitygraph.dto.TaskResponse;
import org.burgas.entitygraph.entity.Employee;
import org.burgas.entitygraph.entity.Task;
import org.burgas.entitygraph.exception.EmployeeNotFoundException;
import org.burgas.entitygraph.exception.TaskNotFoundException;
import org.burgas.entitygraph.mapper.TaskMapper;
import org.burgas.entitygraph.repository.EmployeeRepository;
import org.burgas.entitygraph.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.burgas.entitygraph.message.EmployeeMessages.EMPLOYEE_NOT_FOUND;
import static org.burgas.entitygraph.message.TaskMessages.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = SUPPORTS)
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final EmployeeRepository employeeRepository;

    public List<TaskResponse> findAll() {
        return this.taskRepository.findAll()
                .parallelStream()
                .map(this.taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse findById(final Long taskId) {
        return this.taskRepository.findById(taskId == null ? 0L : taskId)
                .map(this.taskMapper::toResponse)
                .orElseThrow(
                        () -> new TaskNotFoundException(TASK_NOT_FOUND_EXCEPTION.getMessage())
                );
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public TaskResponse createOrUpdate(final TaskRequest taskRequest) {
        return this.taskMapper.toResponse(
                this.taskRepository.save(this.taskMapper.toEntity(taskRequest))
        );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public TaskResponse completeTask(final Long taskId) {
        return this.taskRepository.findByIdPessimisticWrite(taskId == null ? 0L : taskId)
                .map(
                        task -> {
                            task.setDoneAt(LocalDateTime.now());
                            task.setDone(true);
                            return this.taskMapper.toResponse(this.taskRepository.save(task));
                        }
                )
                .orElseThrow(
                        () -> new TaskNotFoundException(TASK_NOT_FOUND_EXCEPTION.getMessage())
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String addEmployeeToTask(final Long taskId, final Long employeeId) {
        return this.taskRepository.findById(taskId == null ? 0L : taskId)
                .map(
                        task -> this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                                .map(
                                        employee -> {
                                            task.addEmployee(employee);
                                            return EMPLOYEE_ADDED_TO_TASK.getMessage();
                                        }
                                )
                                .orElseThrow(
                                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                                )
                )
                .orElseThrow(
                        () -> new TaskNotFoundException(TASK_NOT_FOUND_EXCEPTION.getMessage())
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String removeEmployeeFromTask(final Long taskId, final Long employeeId) {
        Task task = this.taskRepository.findById(taskId == null ? 0L : taskId)
                .orElseThrow(
                        () -> new TaskNotFoundException(TASK_NOT_FOUND_EXCEPTION.getMessage())
                );
        Employee employee = this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                .orElseThrow(
                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                );
        task.removeEmployee(employee);
        return EMPLOYEE_REMOVED_FROM_TASK.getMessage();
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String addEmployeesToTask(final Long taskId, final List<Long> employeeIds) {
        return this.taskRepository.findById(taskId == null ? 0L : taskId)
                .map(
                        task -> {
                            List<Employee> employees = this.employeeRepository.findAllById(employeeIds);
                            task.addEmployees(employees);
                            return EMPLOYEE_LIST_ADDED_TO_TASK.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new TaskNotFoundException(TASK_NOT_FOUND_EXCEPTION.getMessage())
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String removeEmployeesFromTask(final Long taskId, final List<Long> employeeIds) {
        return this.taskRepository.findById(taskId == null ? 0L : taskId)
                .map(
                        task -> {
                            List<Employee> employees = this.employeeRepository.findAllById(employeeIds);
                            task.removeEmployees(employees);
                            return EMPLOYEE_LIST_REMOVED_FROM_TASK.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new TaskNotFoundException(TASK_NOT_FOUND_EXCEPTION.getMessage())
                );
    }
}
