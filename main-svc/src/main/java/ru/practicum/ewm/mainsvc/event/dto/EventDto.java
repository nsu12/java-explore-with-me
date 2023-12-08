package ru.practicum.ewm.mainsvc.event.dto;

public interface EventDto {
    Long getId();

    void setConfirmedRequests(Integer count);

    void setViews(Long count);
}
