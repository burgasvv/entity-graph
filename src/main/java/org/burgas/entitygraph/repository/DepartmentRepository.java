package org.burgas.entitygraph.repository;

import org.burgas.entitygraph.entity.Department;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Override
    @EntityGraph(attributePaths = "employees")
    @NotNull List<Department> findAll();

    @Override
    @EntityGraph(value = "department-with-employees")
    @NotNull Optional<Department> findById(@NotNull Long aLong);
}
