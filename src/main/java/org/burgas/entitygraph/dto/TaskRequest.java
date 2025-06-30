package org.burgas.entitygraph.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public final class TaskRequest extends Request {

    private Long id;
    private String name;
    private String description;
}
