package ru.practicum.ewm.mainsvc.event.dto;

import java.util.Optional;

public enum EventSortOption {
    NO_SORT,
    EVENT_DATE,
    VIEWS;

    public static Optional<EventSortOption> fromString(String string) {
        if (string == null) return Optional.of(NO_SORT);

        for (EventSortOption state: EventSortOption.values()) {
            if (state.name().equalsIgnoreCase(string))
                return Optional.of(state);
        }
        return Optional.empty();
    }
}
