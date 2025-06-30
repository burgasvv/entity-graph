package org.burgas.entitygraph.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "department-with-employees",
        attributeNodes = @NamedAttributeNode(value = "employees")
)
public class Department extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String description;

    @OneToMany(
            mappedBy = "department",
            cascade = {PERSIST, MERGE, DETACH}
    )
    private List<Employee> employees;

    public void addEmployee(Employee employee) {
        this.getEmployees().add(employee);
        employee.setDepartment(this);
    }

    public void removeEmployee(Employee employee) {
        this.getEmployees().remove(employee);
        employee.setDepartment(null);
    }
}
