package ru.practicum.ewm.mainsvc.request;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.mainsvc.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.mainsvc.request.dto.ParticipationRequestDto;

import java.util.List;

@Transactional(readOnly = true)
public interface RequestService {
    @Transactional
    ParticipationRequestDto createRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getAllUserRequests(Long userId);

    @Transactional
    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getAllEventRequests(Long userId, Long eventId);

    @Transactional
    EventRequestStatusUpdateResult updateEventRequestsStatus(Long userId,
                                                             Long eventId,
                                                             EventRequestStatusUpdateRequest updateRequest);
}
