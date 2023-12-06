package ru.practicum.ewm.mainsvc.compilation.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.mainsvc.compilation.model.Compilation;
import ru.practicum.ewm.mainsvc.event.dto.EventMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public static CompilationDto toDto(Compilation compilation) {
        CompilationDto dto = new CompilationDto();
        dto.setEvents(EventMapper.toShortDto(new ArrayList<>(compilation.getEvents())));
        dto.setId(compilation.getId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.getPinned());
        return dto;
    }

    public static List<CompilationDto> toDto(List<Compilation> compilations) {
        if (compilations == null || compilations.isEmpty()) return Collections.emptyList();
        return compilations.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }
}
