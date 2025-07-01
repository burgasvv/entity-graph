package org.burgas.entitygraph.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "task-with-employees",
        attributeNodes = @NamedAttributeNode(value = "employees")
)
public class Task extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime doneAt;
    private Boolean done;

    @ManyToMany(mappedBy = "tasks", cascade = {PERSIST, MERGE}, fetch = EAGER)
    private List<Employee> employees;

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.getTasks().add(this);
    }

    public void removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getTasks().remove(this);
    }

    public void addEmployees(List<Employee> employees) {
        this.employees.addAll(employees);
        employees.forEach(employee -> employee.getTasks().add(this));
    }

    public void removeEmployees(List<Employee> employees) {
        this.employees.removeAll(employees);
        employees.forEach(employee -> employee.getTasks().remove(this));
    }
}
