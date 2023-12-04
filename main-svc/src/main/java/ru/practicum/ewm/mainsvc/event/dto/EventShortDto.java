package ru.practicum.ewm.mainsvc.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.mainsvc.category.dto.CategoryDto;
import ru.practicum.ewm.mainsvc.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class EventShortDto implements EventDto {
    private Long id;
    private String title;
    private String annotation;
    private String description;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private boolean paid;
    private Long views;
}
