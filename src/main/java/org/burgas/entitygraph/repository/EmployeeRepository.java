package org.burgas.entitygraph.repository;

import org.burgas.entitygraph.entity.Employee;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Override
    @EntityGraph(value = "employee-with-department-and-tasks")
    @NotNull List<Employee> findAll();

    @Override
    @EntityGraph(value = "employee-with-department-and-tasks")
    @NotNull Optional<Employee> findById(@NotNull Long aLong);

    @Query(
            nativeQuery = true,
            value = """
                    select e.* from employee e join public.task_employee te on e.id = te.employee_id
                                        where te.task_id = :taskId
                    """
    )
    List<Employee> findEmployeesByTaskId(Long taskId);
}
