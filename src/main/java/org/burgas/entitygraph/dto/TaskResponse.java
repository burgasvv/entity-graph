package org.burgas.entitygraph.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public final class TaskResponse extends Response {

    private Long id;
    private String name;
    private String description;
    private String createdAt;
    private String doneAt;
    private Boolean done;
    private List<EmployeeResponse> employees;
}
