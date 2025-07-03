package org.burgas.entitygraph.repository;

import jakarta.persistence.LockModeType;
import org.burgas.entitygraph.entity.Task;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Override
    @EntityGraph(value = "task-with-employees-and-department")
    @NotNull List<Task> findAll();

    @Override
    @EntityGraph(value = "task-with-employees-and-department")
    @NotNull List<Task> findAllById(@NotNull Iterable<Long> longs);

    @Override
    @EntityGraph(value = "task-with-employees-and-department")
    @NotNull Optional<Task> findById(@NotNull Long aLong);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select t from org.burgas.entitygraph.entity.Task t join fetch t.employees where t.id = :aLong")
    Optional<Task> findByIdPessimisticWrite(Long taskId);
}
