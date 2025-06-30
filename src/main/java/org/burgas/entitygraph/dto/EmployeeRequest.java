package org.burgas.entitygraph.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public final class EmployeeRequest extends Request {

    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private Long departmentId;
}
