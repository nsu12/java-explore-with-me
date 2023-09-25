package ru.practicum.ewm.stats.dto;

import ru.practicum.ewm.stats.model.EndpointHit;

public class EndpointHitMapper {
    public static EndpointHitDto toDto(EndpointHit hit) {
        return new EndpointHitDto(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }
}
