package ru.practicum.ewm.mainsvc.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.category.CategoryRepository;
import ru.practicum.ewm.mainsvc.category.model.Category;
import ru.practicum.ewm.mainsvc.error.EntryNotFoundException;
import ru.practicum.ewm.mainsvc.event.dto.*;
import ru.practicum.ewm.mainsvc.event.model.Event;
import ru.practicum.ewm.mainsvc.user.UserRepository;
import ru.practicum.ewm.mainsvc.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
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
        event.setConfirmedRequests(0L);
        event.setState(EventState.PENDING);
        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getAllUserEvents(Long userId, Integer from, Integer size) {
        List<EventShortDto> events = EventMapper.toShortDto(
                eventRepository.findAllByInitiator_Id(
                        userId, PageRequest.of(from / size, size)
                ).toList()
        );
        // todo: append views count
        return events;
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        EventFullDto event = EventMapper.toFullDto(
                eventRepository.findByIdAndInitiator_Id(eventId, userId)
                        .orElseThrow(() -> new EntryNotFoundException(
                                        String.format("Event with id=%d was not found", eventId)
                                )
                        )
        );
        // todo: append views count
        return event;
    }

    @Override
    public EventFullDto updateEventFromUser(Long userId, Long eventId, UpdateEventUserRequest eventRequest) {
        return null;
    }

    @Override
    public List<EventShortDto> getAllEvents(String text,
                                            List<Long> categories,
                                            Boolean paid,
                                            LocalDateTime rangeStartDate, LocalDateTime rangeEndDate,
                                            Boolean onlyAvailable,
                                            EventSortOption sortBy,
                                            Integer from, Integer size) {
        return null;
    }

    @Override
    public EventFullDto getEventById(Long id) {
        return null;
    }

    @Override
    public List<EventFullDto> findEvents(List<Long> users,
                                         List<String> states,
                                         List<Long> categories,
                                         LocalDateTime rangeStartDate, LocalDateTime rangeEndDate,
                                         Integer from, Integer size) {
        return null;
    }

    @Override
    public EventFullDto updateEventFromAdmin(Long eventId, UpdateEventAdminRequest eventRequest) {
        return null;
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
}
