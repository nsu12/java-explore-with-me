package ru.practicum.ewm.mainsvc.event;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.event.dto.*;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface EventService {
    @Transactional
    EventFullDto createEvent(Long userId, NewEventDto newEvent);

    List<EventShortDto> getAllUserEvents(Long userId, Integer from, Integer size);

    EventFullDto getUserEvent(Long userId, Long eventId);

    @Transactional
    EventFullDto updateEventFromUser(Long userId, Long eventId, UpdateEventUserRequest eventRequest);

    List<EventShortDto> findPublishedEvents(String text,
                                            List<Long> categories,
                                            Boolean paid,
                                            LocalDateTime rangeStartDate, LocalDateTime rangeEndDate,
                                            Boolean onlyAvailable,
                                            EventSortOption sortBy,
                                            Integer from, Integer size);

    EventFullDto getPublishedEventById(Long id);

    List<EventFullDto> findEvents(List<Long> users,
                                  List<String> states,
                                  List<Long> categories,
                                  LocalDateTime rangeStartDate, LocalDateTime rangeEndDate,
                                  Integer from, Integer size);

    @Transactional
    EventFullDto updateEventFromAdmin(Long eventId, UpdateEventAdminRequest eventRequest);
}
