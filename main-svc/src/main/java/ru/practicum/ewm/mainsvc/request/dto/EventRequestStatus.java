package ru.practicum.ewm.mainsvc.request.dto;

import java.util.Optional;

public enum EventRequestStatus {
    PENDING,
    CONFIRMED,
    REJECTED;

    public static Optional<EventRequestStatus> fromString(String string) {
        for (EventRequestStatus state: EventRequestStatus.values()) {
            if (state.name().equalsIgnoreCase(string))
                return Optional.of(state);
        }
        return Optional.empty();
    }
}
