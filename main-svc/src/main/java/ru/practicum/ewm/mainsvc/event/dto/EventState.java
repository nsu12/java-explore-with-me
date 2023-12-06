package ru.practicum.ewm.mainsvc.event.dto;

import java.util.Optional;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static Optional<EventState> fromString(String string) {
        for (EventState state: EventState.values()) {
            if (state.name().equalsIgnoreCase(string))
                return Optional.of(state);
        }
        return Optional.empty();
    }
}
