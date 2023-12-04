package ru.practicum.ewm.mainsvc.event.dto;

public interface EventDto {
    Long getId();

    void setConfirmedRequests(Long count);
}
