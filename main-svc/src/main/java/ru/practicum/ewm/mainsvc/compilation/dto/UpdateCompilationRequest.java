package ru.practicum.ewm.mainsvc.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import ru.practicum.ewm.mainsvc.event.dto.EventShortDto;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateCompilationRequest {
    @UniqueElements
    private List<EventShortDto> events;
    private boolean pinned;
    @Size(min = 1, max = 50)
    private String title;
}
