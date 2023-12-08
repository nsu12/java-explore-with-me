package ru.practicum.ewm.mainsvc.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.mainsvc.event.dto.EventShortDto;

import java.util.List;

@Data
@NoArgsConstructor
public class CompilationDto {
    private List<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;
}
