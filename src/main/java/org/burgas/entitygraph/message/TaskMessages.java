package org.burgas.entitygraph.message;

import lombok.Getter;

@Getter
public enum TaskMessages {

    EMPLOYEE_LIST_ADDED_TO_TASK("Employee list was added to task"),
    EMPLOYEE_LIST_REMOVED_FROM_TASK("Employee list was removed from task"),
    EMPLOYEE_ADDED_TO_TASK("Employee was added to task"),
    EMPLOYEE_REMOVED_FROM_TASK("Employee was removed from task"),
    TASK_FIELD_NAME_EMPTY("Task field name is empty"),
    TASK_FIELD_DESCRIPTION_EMPTY("Task field description empty"),
    TASK_NOT_FOUND_EXCEPTION("Task not found");

    private final String message;

    TaskMessages(String message) {
        this.message = message;
    }
}
