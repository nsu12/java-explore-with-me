package ru.practicum.ewm.mainsvc.compilation;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.compilation.dto.CompilationDto;
import ru.practicum.ewm.mainsvc.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.mainsvc.compilation.dto.UpdateCompilationRequest;

import java.util.List;

@Transactional(readOnly = true)
public interface CompilationService {
    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto get(Long compId);

    @Transactional
    CompilationDto create(NewCompilationDto newCompilation);

    @Transactional
    CompilationDto update(Long compId, UpdateCompilationRequest request);

    @Transactional
    void delete(Long compId);
}
