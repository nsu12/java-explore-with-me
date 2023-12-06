package ru.practicum.ewm.mainsvc.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.error.EntryNotFoundException;
import ru.practicum.ewm.mainsvc.error.InvalidRequestParamsException;
import ru.practicum.ewm.mainsvc.event.EventRepository;
import ru.practicum.ewm.mainsvc.event.dto.EventState;
import ru.practicum.ewm.mainsvc.event.model.Event;
import ru.practicum.ewm.mainsvc.request.dto.*;
import ru.practicum.ewm.mainsvc.request.model.Request;
import ru.practicum.ewm.mainsvc.user.UserRepository;
import ru.practicum.ewm.mainsvc.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        Event event = getEventOrThrow(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new InvalidRequestParamsException(
                    "Cannot create request - event not published"
            );
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new InvalidRequestParamsException(
                "Event initiator cannot make participation request"
            );
        }

        if (event.getParticipantLimit() != 0) {
            Integer requestsCount =
                    requestRepository.countAllByEvent_IdAndStatusIs(eventId, EventRequestStatus.CONFIRMED);
            if (event.getParticipantLimit().equals(requestsCount)) {
                throw new InvalidRequestParamsException(
                        "Cannot create request - participant limit reached"
                );
            }
        }

        Request request = new Request();
        request.setRequester(getUserOrThrow(userId));
        request.setEvent(event);
        request.setCreatedOn(LocalDateTime.now());

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(EventRequestStatus.CONFIRMED);
        } else {
            request.setStatus(EventRequestStatus.PENDING);
        }

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getAllUserRequests(Long userId) {
        getUserOrThrow(userId);
        return RequestMapper.toDto(requestRepository.findAllByRequester_Id(userId));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        getUserOrThrow(userId);
        Request request = requestRepository.findByIdAndRequester_Id(requestId, userId)
                .orElseThrow(() -> new EntryNotFoundException(
                        String.format("Request with id=%d not found", requestId)
                    )
                );
        request.setStatus(EventRequestStatus.CANCELED);
        return RequestMapper.toDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getAllRequestsForUserEvent(Long userId, Long eventId) {
        if (eventRepository.findByIdAndInitiator_Id(eventId, userId).isEmpty()) {
            return Collections.emptyList();
        }
        return RequestMapper.toDto(requestRepository.findAllByEvent_Id(eventId));
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventRequestsStatuses(Long userId,
                                                                      Long eventId,
                                                                      EventRequestStatusUpdateRequest updateRequest) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EntryNotFoundException(
                                String.format("Event with id=%d was not found", eventId)
                        )
                );

        int confirmedRequestsCount = 0;
        var newStatus = EventRequestStatus.fromString(updateRequest.getStatus()).orElseThrow(); // already validated
        if (newStatus == EventRequestStatus.CONFIRMED && event.getParticipantLimit() != 0) {
            confirmedRequestsCount = requestRepository.countAllByEvent_IdAndStatusIs(
                    eventId, EventRequestStatus.CONFIRMED
            );
            if (confirmedRequestsCount >= event.getParticipantLimit())  {
                throw new InvalidRequestParamsException(
                        "The participant limit has been reached"
                );
            }
        }

        Integer countNotPending = requestRepository.countAllByEvent_IdAndStatusNotAndIdIn(
                eventId, EventRequestStatus.PENDING, updateRequest.getRequestIds()
        );
        if (countNotPending != 0) {
            throw new InvalidRequestParamsException(
                    "Request status can be changed only in pending requests"
            );
        }

        List<Request> requests = requestRepository.findAllByEvent_IdAndStatusIsAndIdIn(
                eventId, EventRequestStatus.PENDING, updateRequest.getRequestIds()
        );

        var result = new EventRequestStatusUpdateResult();

        if (newStatus == EventRequestStatus.REJECTED) {
            result.setRejectedRequests(
                    RequestMapper.toDto(
                            requestRepository.saveAll(
                                    requests.stream()
                                            .peek(request -> request.setStatus(EventRequestStatus.REJECTED))
                                            .collect(Collectors.toList())
                            )
                    )
            );
        }

        if (newStatus == EventRequestStatus.CONFIRMED) {
            int numToConfirm = event.getParticipantLimit() == 0? requests.size():
                    Math.min(event.getParticipantLimit() - confirmedRequestsCount, requests.size());
            result.setConfirmedRequests(
                    RequestMapper.toDto(
                            requestRepository.saveAll(
                                    requests.stream().limit(numToConfirm)
                                            .peek(request -> request.setStatus(EventRequestStatus.CONFIRMED))
                                            .collect(Collectors.toList())
                            )
                    )
            );
            result.setRejectedRequests(
                    RequestMapper.toDto(
                            requestRepository.saveAll(
                                    requests.stream().skip(numToConfirm)
                                            .peek(request -> request.setStatus(EventRequestStatus.REJECTED))
                                            .collect(Collectors.toList())
                            )
                    )
            );
        }

        return result;
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException(
                                String.format("User with id=%d not found", id)
                        )
                );
    }

    private Event getEventOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException(
                        String.format("Event with id=%d not found", id)
                    )
                );
    }
}
