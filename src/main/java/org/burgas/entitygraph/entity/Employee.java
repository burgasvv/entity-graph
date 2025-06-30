package org.burgas.entitygraph.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "employee-with-department-and-tasks",
        attributeNodes = {
                @NamedAttributeNode(value = "department"),
                @NamedAttributeNode(value = "tasks")
        }
)
public class Employee extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String patronymic;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany
    @JoinTable(
            name = "task_employee",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks;

    public void addTask(Task task) {
        this.tasks.add(task);
        task.getEmployees().add(this);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
        task.getEmployees().remove(this);
    }

    public void addTasks(List<Task> tasks) {
        this.tasks.addAll(tasks);
        tasks.forEach(
                task -> task.getEmployees().add(this)
        );
    }

    public void removeTasks(List<Task> tasks) {
        this.tasks.removeAll(tasks);
        tasks.forEach(
                task -> task.getEmployees().remove(this)
        );
    }
}
