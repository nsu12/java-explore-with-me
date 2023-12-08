package ru.practicum.ewm.mainsvc.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainsvc.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.mainsvc.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.mainsvc.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class RequestController {

    private final RequestService requestService;

    @PostMapping(value = "/users/{userId}/requests")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(
            @PathVariable Long userId,
            @RequestParam(name = "eventId") Long eventId
    ) {
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping(value = "/users/{userId}/requests")
    public List<ParticipationRequestDto> getAllUserRequests(@PathVariable Long userId) {
        return requestService.getAllUserRequests(userId);
    }

    @PatchMapping(value = "/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) {
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping(value = "/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getAllRequestsForUserEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return requestService.getAllRequestsForUserEvent(userId, eventId);
    }

    @PatchMapping(value = "/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventParticipationRequestsStatuses(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest
    ) {
        return requestService.updateEventRequestsStatuses(userId, eventId, updateRequest);
    }
}
