package ru.practicum.ewm.mainsvc.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.compilation.dto.CompilationDto;
import ru.practicum.ewm.mainsvc.compilation.dto.CompilationMapper;
import ru.practicum.ewm.mainsvc.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.mainsvc.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.mainsvc.compilation.model.Compilation;
import ru.practicum.ewm.mainsvc.error.EntryNotFoundException;
import ru.practicum.ewm.mainsvc.event.EventRepository;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto newCompilation) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilation.getTitle());
        compilation.setPinned(newCompilation.isPinned());
        if (newCompilation.getEvents() != null) {
            compilation.setEvents(new HashSet<>(eventRepository.findAllById(newCompilation.getEvents())));
        }
        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        return CompilationMapper.toDto(
                compilationRepository.findAllBy(
                    pinned, PageRequest.of(from / size, size)
                ).toList()
        );
    }

    @Override
    public CompilationDto get(Long compId) {
        return CompilationMapper.toDto(getCompilationOrThrow(compId));
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationRequest request) {
        Compilation compilation = getCompilationOrThrow(compId);
        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }
        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }
        if (request.getEvents() != null) {
            compilation.setEvents(new HashSet<>(eventRepository.findAllById(request.getEvents())));
        }
        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        compilationRepository.delete(getCompilationOrThrow(compId));
    }

    private Compilation getCompilationOrThrow(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntryNotFoundException(
                                String.format("Compilation with id=%d was not found", compId)
                        )
                );
    }
}
