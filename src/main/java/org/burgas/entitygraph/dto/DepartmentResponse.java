package org.burgas.entitygraph.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public final class DepartmentResponse extends Response{

    private Long id;
    private String name;
    private String description;
    private List<EmployeeResponse> employees;
}
