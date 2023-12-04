package ru.practicum.ewm.mainsvc.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.mainsvc.validation.NotEarlyThan;
import ru.practicum.ewm.mainsvc.validation.ValueOfEnum;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UpdateEventUserRequest implements UpdateEventRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    @NotEarlyThan(hours = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @ValueOfEnum(enumClass = EventUserStateAction.class)
    private String stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
