package ru.practicum.ewm.mainsvc.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.mainsvc.request.model.Request;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {

    public static ParticipationRequestDto toDto(Request request) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(request.getId());
        dto.setRequester(request.getRequester().getId());
        dto.setEvent(request.getEvent().getId());
        dto.setCreated(request.getCreatedOn());
        dto.setStatus(request.getStatus().toString());
        return dto;
    }

    public static List<ParticipationRequestDto> toDto(List<Request> requests) {
        if (requests == null || requests.isEmpty()) return Collections.emptyList();
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
