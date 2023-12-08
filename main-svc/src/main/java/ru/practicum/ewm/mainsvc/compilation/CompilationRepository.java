package ru.practicum.ewm.mainsvc.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.mainsvc.compilation.model.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(
        "SELECT c FROM Compilation c " +
        "WHERE :pinned IS NULL OR c.pinned = :pinned"
    )
    Page<Compilation> findAllBy(Boolean pinned, PageRequest pageRequest);
}
