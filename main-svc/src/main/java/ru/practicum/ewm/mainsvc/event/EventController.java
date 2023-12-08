package ru.practicum.ewm.mainsvc.event;

import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainsvc.error.InvalidRequestParamsException;
import ru.practicum.ewm.mainsvc.event.dto.*;
import ru.practicum.ewm.mainsvc.validation.ValueOfEnum;
import ru.practicum.ewm.stats.client.StatsClient;
import ru.practicum.ewm.stats.dto.EndpointHitInDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ComponentScan("ru.practicum.ewm.stats")
@RestController
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;
    private final StatsClient statsClient;

    // private
    @PostMapping(value = "/users/{userId}/events")
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventFullDto create(
            @PathVariable Long userId,
            @RequestBody @Valid NewEventDto eventInDto) {
        return eventService.createEvent(userId, eventInDto);
    }

    @GetMapping(value = "/users/{userId}/events")
    public List<EventShortDto> getAllUserEvents(
            @PathVariable Long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return eventService.getAllUserEvents(userId, from, size);
    }

    @GetMapping(value = "/users/{userId}/events/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping(value = "/users/{userId}/events/{eventId}")
    public EventFullDto updateEventFromUser(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventUserRequest eventRequest
    ) {
        return eventService.updateEventFromUser(userId, eventId, eventRequest);
    }

    // public
    @GetMapping(value = "/events")
    public List<EventShortDto> getPublishedEvents(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(name = "sort", required = false) String sortBy,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        var range = checkDateRange(rangeStart, rangeEnd);

        statsClient.addHit(
                new EndpointHitInDto(
                    "main-svc", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now()
                )
        );

        return eventService.findPublishedEvents(
                text,
                categories,
                paid,
                range.getValue0(), range.getValue1(),
                onlyAvailable,
                EventSortOption.fromString(sortBy).orElseThrow(
                        () -> new InvalidRequestParamsException(String.format("Unknown sort option: %s", sortBy))
                ),
                from,
                size
        );
    }

    @GetMapping(value = "/events/{id}")
    public EventFullDto getPublishedEventById(@PathVariable Long id, HttpServletRequest request) {
        statsClient.addHit(
                new EndpointHitInDto(
                        "main-svc", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now()
                )
        );
        return eventService.getPublishedEventById(id);
    }

    // admin
    @GetMapping(value = "/admin/events")
    public List<EventFullDto> findEvents(
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "states", required = false) List<@ValueOfEnum(enumClass = EventState.class) String> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        var range = checkDateRange(rangeStart, rangeEnd);

        return eventService.findEvents(
                users,
                states,
                categories,
                range.getValue0(), range.getValue1(),
                from,
                size
        );
    }

    @PatchMapping(value = "/admin/events/{eventId}")
    public EventFullDto updateEventFromAdmin(
            @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventAdminRequest eventRequest
    ) {
        return eventService.updateEventFromAdmin(eventId, eventRequest);
    }

    private Pair<LocalDateTime, LocalDateTime> checkDateRange(String rangeStart, String rangeEnd) {
        LocalDateTime rangeStartDate = null;
        LocalDateTime rangeEndDate = null;

        if (rangeStart != null) {
            rangeStartDate = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (rangeEnd == null) {
                throw new ValidationException("range end date must be set");
            }
        }

        if (rangeEnd != null) {
            rangeEndDate = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (rangeStart == null) {
                throw new ValidationException("range start date must be set");
            }
        }

        if (rangeEnd != null && !rangeEndDate.isAfter(rangeStartDate)) {
            throw new ValidationException("range end date must be after start date");
        }

        return new Pair<>(rangeStartDate, rangeEndDate);
    }
}
