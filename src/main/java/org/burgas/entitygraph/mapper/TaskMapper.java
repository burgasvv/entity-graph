package org.burgas.entitygraph.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.entitygraph.dto.TaskRequest;
import org.burgas.entitygraph.dto.TaskResponse;
import org.burgas.entitygraph.entity.Task;
import org.burgas.entitygraph.repository.TaskRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.burgas.entitygraph.message.TaskMessages.TASK_FIELD_DESCRIPTION_EMPTY;
import static org.burgas.entitygraph.message.TaskMessages.TASK_FIELD_NAME_EMPTY;

@Component
@RequiredArgsConstructor
public final class TaskMapper implements EntityMapper<TaskRequest, Task, TaskResponse> {

    private final TaskRepository taskRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public Task toEntity(TaskRequest taskRequest) {
        Long taskId = this.handleData(taskRequest.getId(), 0L);
        return this.taskRepository.findById(taskId)
                .map(
                        task -> Task.builder()
                                .id(task.getId())
                                .name(this.handleData(taskRequest.getName(), task.getName()))
                                .description(this.handleData(taskRequest.getDescription(), task.getDescription()))
                                .createdAt(task.getCreatedAt())
                                .doneAt(task.getDoneAt())
                                .done(task.getDone())
                                .build()
                )
                .orElseGet(
                        () -> Task.builder()
                                .name(this.handleDataThrowable(taskRequest.getName(), TASK_FIELD_NAME_EMPTY.getMessage()))
                                .description(this.handleDataThrowable(taskRequest.getDescription(), TASK_FIELD_DESCRIPTION_EMPTY.getMessage()))
                                .createdAt(LocalDateTime.now())
                                .doneAt(null)
                                .done(null)
                                .build()
                );
    }

    @Override
    public TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .createdAt(task.getCreatedAt().format(ofPattern("dd MMMM yyyy, hh:mm:ss")))
                .doneAt(task.getDoneAt() == null ? "" : task.getDoneAt().format(ofPattern("dd MMMM yyyy, hh:mm:ss")))
                .done(task.getDone())
                .employees(
                        task.getEmployees() == null ? null : task.getEmployees()
                                .parallelStream()
                                .map(this.employeeMapper::toResponse)
                                .toList()
                )
                .build();
    }
}
