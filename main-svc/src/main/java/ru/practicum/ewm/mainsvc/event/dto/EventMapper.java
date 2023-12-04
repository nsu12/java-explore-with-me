package ru.practicum.ewm.mainsvc.event.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.mainsvc.category.dto.CategoryMapper;
import ru.practicum.ewm.mainsvc.event.model.Event;
import ru.practicum.ewm.mainsvc.user.dto.UserMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {

    public static EventFullDto toFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .category(CategoryMapper.toDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .location(new LocationDto(event.getLocationLat(), event.getLocationLon()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .build();
    }

    public static List<EventFullDto> toFullDto(List<Event> events) {
        if (events == null || events.isEmpty()) return Collections.emptyList();
        return events.stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    public static EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .category(CategoryMapper.toDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .build();
    }

    public static List<EventShortDto> toShortDto(List<Event> events) {
        if (events == null || events.isEmpty()) return Collections.emptyList();
        return events.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }
}
