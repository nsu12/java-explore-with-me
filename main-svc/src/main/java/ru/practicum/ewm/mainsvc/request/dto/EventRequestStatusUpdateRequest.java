package ru.practicum.ewm.mainsvc.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import ru.practicum.ewm.mainsvc.validation.ValueOfEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    @NotNull
    @NotEmpty
    @UniqueElements
    private List<Long> requestIds;
    @ValueOfEnum(enumClass = EventRequestStatus.class)
    private String status;
}
