package ru.practicum.ewm.mainsvc.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.category.CategoryRepository;
import ru.practicum.ewm.mainsvc.category.model.Category;
import ru.practicum.ewm.mainsvc.error.EntryNotFoundException;
import ru.practicum.ewm.mainsvc.error.InvalidRequestParamsException;
import ru.practicum.ewm.mainsvc.event.dto.*;
import ru.practicum.ewm.mainsvc.event.model.Event;
import ru.practicum.ewm.mainsvc.request.RequestCountView;
import ru.practicum.ewm.mainsvc.request.RequestRepository;
import ru.practicum.ewm.mainsvc.user.UserRepository;
import ru.practicum.ewm.mainsvc.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        Event event = new Event();
        event.setInitiator(getUserOrThrow(userId));
        event.setTitle(newEventDto.getTitle());
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setCategory(getCategoryOrThrow(newEventDto.getCategory()));
        event.setEventDate(newEventDto.getEventDate());
        event.setLocationLat(newEventDto.getLocation().getLat());
        event.setLocationLon(newEventDto.getLocation().getLon());
        event.setPaid(newEventDto.isPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getAllUserEvents(Long userId, Integer from, Integer size) {
        getUserOrThrow(userId);
        List<EventShortDto> dtoList = EventMapper.toShortDto(eventRepository.findAllByInitiator_Id(
                        userId, PageRequest.of(from / size, size)
                ).toList()
        );

        setupConfirmedRequests(dtoList);

        return dtoList;
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        getUserOrThrow(userId);
        EventFullDto eventDto = EventMapper.toFullDto(
                eventRepository.findByIdAndInitiator_Id(eventId, userId)
                        .orElseThrow(() -> new EntryNotFoundException(
                                        String.format("Event with id=%d was not found", eventId)
                                )
                        )
        );

        setupConfirmedRequests(List.of(eventDto));

        return eventDto;
    }

    @Override
    @Transactional
    public EventFullDto updateEventFromUser(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        getUserOrThrow(userId);
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(
                        () -> new EntryNotFoundException(
                                String.format("Event with id=%d was not found", eventId)
                        )
                );

        if (event.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new InvalidRequestParamsException(
                    "Cannot change the event because the start date of the modified " +
                            "event must be no earlier than two hours from the current moment"
            );
        }

        if (event.getState() == EventState.PUBLISHED) {
            throw new InvalidRequestParamsException("Only pending or canceled events can be changed");
        }

        processUpdateEventRequest(event, updateRequest);

        if (updateRequest.getStateAction() != null) {
            switch (EventUserStateAction.valueOf(updateRequest.getStateAction())) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }

        EventFullDto dto = EventMapper.toFullDto(eventRepository.save(event));
        setupConfirmedRequests(List.of(dto));
        return dto;
    }

    @Override
    public List<EventShortDto> findPublishedEvents(String text,
                                                   List<Long> categories,
                                                   Boolean paid,
                                                   LocalDateTime rangeStartDate, LocalDateTime rangeEndDate,
                                                   Boolean onlyAvailable,
                                                   EventSortOption sortBy,
                                                   Integer from, Integer size) {
        List<Event> eventList = eventRepository.findPublishedEventsBy(
                text,
                categories,
                paid,
                rangeStartDate == null ? LocalDateTime.now() : rangeStartDate,
                rangeEndDate,
                PageRequest.of(from / size, size)
        ).toList();

        Map<Long, Integer> requestsCountForEvents = getRequestsCountForEvents(
                eventList.stream().map(Event::getId).collect(Collectors.toList())
        );

        List<EventShortDto> dtoList;
        if (onlyAvailable == null || !onlyAvailable) {
            dtoList = EventMapper.toShortDto(eventList);
        } else {
            dtoList = EventMapper.toShortDto(
                    eventList.stream().filter(
                            event -> requestsCountForEvents
                                    .getOrDefault(event.getId(), 0) < event.getParticipantLimit()
                    ).collect(Collectors.toList())
            );
        }

        switch (sortBy) {
            case EVENT_DATE:
                dtoList.sort(Comparator.comparing(EventShortDto::getEventDate));
                break;
            case VIEWS:
                break;
            default:
                break;
        }

        return dtoList;
    }

    @Override
    public EventFullDto getPublishedEventById(Long id) {
        EventFullDto eventDto = EventMapper.toFullDto(
                eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                        .orElseThrow(() -> new EntryNotFoundException(
                                        String.format("Event with id=%d was not found", id)
                                )
                        )
        );

        setupConfirmedRequests(List.of(eventDto));

        return eventDto;
    }

    @Override
    public List<EventFullDto> findEvents(List<Long> users,
                                         List<String> states,
                                         List<Long> categories,
                                         LocalDateTime rangeStartDate,
                                         LocalDateTime rangeEndDate,
                                         Integer from, Integer size) {
        List<EventState> eventStates = null;
        if (states != null) {
            eventStates = states.stream()
                    .map(EventState::fromString)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        }

        List<EventFullDto> eventDtoList = EventMapper.toFullDto(eventRepository.findAllBy(
                users,
                eventStates,
                categories,
                rangeStartDate,
                rangeEndDate,
                PageRequest.of(from/size, size)
        ).toList());

        setupConfirmedRequests(eventDtoList);

        return eventDtoList;
    }

    @Override
    @Transactional
    public EventFullDto updateEventFromAdmin(Long eventId, UpdateEventAdminRequest updateRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntryNotFoundException(
                            String.format("Event with id=%d was not found", eventId)
                        )
                );

        LocalDateTime publishDate = event.getPublishedOn() != null? event.getPublishedOn(): LocalDateTime.now();
        if (event.getEventDate().minusHours(1).isBefore(publishDate)) {
            throw new InvalidRequestParamsException(
                    "Cannot change the event because the start date of the modified " +
                    "event must be no earlier than an hour from the publication date"
            );
        }

        processUpdateEventRequest(event, updateRequest);

        if (updateRequest.getStateAction() != null) {

            if (event.getState() != EventState.PENDING) {
                throw new InvalidRequestParamsException(
                        String.format("Cannot publish the event because it's not in the right state: %s", event.getState())
                );
            }

            switch (EventAdminStateAction.valueOf(updateRequest.getStateAction())) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(publishDate);
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }

        EventFullDto eventDto =  EventMapper.toFullDto(eventRepository.save(event));
        setupConfirmedRequests(List.of(eventDto));
        return eventDto;
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException(
                                String.format("Category with id=%d was not found", id)
                        )
                );
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException(
                                String.format("User with id=%d was not found", id)
                        )
                );
    }

    private Map<Long, Integer> getRequestsCountForEvents(List<Long> eventIdList) {
        return requestRepository.getConfirmedRequestCountViewsFor(eventIdList).stream()
                .collect(
                        Collectors.toMap(
                                RequestCountView::getEventId,
                                RequestCountView::getRequestCount
                        )
                );
    }

    private <T extends EventDto> void setupConfirmedRequests(List<T> dtoList) {
        if (dtoList.isEmpty()) return;
        Map<Long, Integer> requestsCountForEvents = getRequestsCountForEvents(
                dtoList.stream().map(T::getId).collect(Collectors.toList())
        );

        for (T dto: dtoList) {
            dto.setConfirmedRequests(requestsCountForEvents.getOrDefault(dto.getId(), 0));
        }
    }

    private <T extends UpdateEventRequest> void processUpdateEventRequest(Event event, T updateRequest) {
        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            event.setCategory(getCategoryOrThrow(updateRequest.getCategory()));
        }
        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getEventDate() != null) {
            event.setEventDate(updateRequest.getEventDate());
        }
        if (updateRequest.getLocation() != null) {
            event.setLocationLat(updateRequest.getLocation().getLat());
            event.setLocationLon(updateRequest.getLocation().getLon());
        }
        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }
    }
}
