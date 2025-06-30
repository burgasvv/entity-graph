package org.burgas.entitygraph.message;

import lombok.Getter;

@Getter
public enum DepartmentMessages {

    EMPLOYEE_REMOVED_FROM_DEPARTMENT("Employee removed from department"),
    EMPLOYEE_ADDED_TO_DEPARTMENT("Employee was added to department"),
    DEPARTMENT_NOT_FOUND("Department not found"),
    DEPARTMENT_DELETED("Department successfully deleted"),
    DEPARTMENT_FIELD_NAME_EMPTY("Department field name is empty"),
    DEPARTMENT_FIELD_DESCRIPTION_EMPTY("Department field description is empty");

    private final String message;

    DepartmentMessages(String message) {
        this.message = message;
    }
}
