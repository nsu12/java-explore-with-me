package ru.practicum.ewm.mainsvc.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.mainsvc.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.mainsvc.request.dto.ParticipationRequestDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        return null;
    }

    @Override
    public List<ParticipationRequestDto> getAllUserRequests(Long userId) {
        return null;
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        return null;
    }

    @Override
    public List<ParticipationRequestDto> getAllEventRequests(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequestsStatus(Long userId,
                                                                    Long eventId,
                                                                    EventRequestStatusUpdateRequest updateRequest) {
        return null;
    }
}
