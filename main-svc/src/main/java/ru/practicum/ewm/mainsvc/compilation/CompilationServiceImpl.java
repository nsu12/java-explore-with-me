package ru.practicum.ewm.mainsvc.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.compilation.dto.CompilationDto;
import ru.practicum.ewm.mainsvc.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.mainsvc.compilation.dto.UpdateCompilationRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        return null;
    }

    @Override
    public CompilationDto get(Long compId) {
        return null;
    }

    @Override
    public CompilationDto create(NewCompilationDto newCompilation) {
        return null;
    }

    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest request) {
        return null;
    }

    @Override
    public void delete(Long compId) {

    }
}
