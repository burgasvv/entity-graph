package org.burgas.entitygraph.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public final class DepartmentRequest extends Request {

    private Long id;
    private String name;
    private String description;
}
