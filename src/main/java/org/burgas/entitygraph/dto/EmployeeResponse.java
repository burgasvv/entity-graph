package org.burgas.entitygraph.dto;

import lombok.*;

@Getter
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public final class EmployeeResponse extends Response {

    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String department;
}
