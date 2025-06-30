package org.burgas.entitygraph.message;

import lombok.Getter;

@Getter
public enum EmployeeMessages {

    TASK_LIST_ADDED_FOR_EMPLOYEE("Task list added for employee"),
    TASK_LIST_REMOVED_FOR_EMPLOYEE("Task list removed for employee"),
    TASK_LIST_EMPTY("Task list is empty"),
    TASK_ADDED_FOR_EMPLOYEE("Task added for employee"),
    TASK_REMOVED_FOR_EMPLOYEE("Task removed for employee"),
    DEPARTMENT_ADDED_FOR_EMPLOYEE("Department added for employee"),
    DEPARTMENT_REMOVED_FOR_EMPLOYEE("Department removed for employee"),
    EMPLOYEE_NOT_FOUND("Employee not found"),
    EMPLOYEE_FIELD_NAME_EMPTY("Employee field name is empty"),
    EMPLOYEE_FIELD_SURNAME_EMPTY("Employee field surname is empty"),
    EMPLOYEE_FIELD_PATRONYMIC_EMPTY("Employee field patronymic is empty"),
    EMPLOYEE_FIELD_DEPARTMENT_EMPTY("Employee field patronymic is empty");

    private final String message;

    EmployeeMessages(String message) {
        this.message = message;
    }
}
