package ru.practicum.ewm.mainsvc.event.dto;

import java.util.Optional;

public enum EventUserStateAction {
    SEND_TO_REVIEW,
    CANCEL_REVIEW;

    public static Optional<EventUserStateAction> fromString(String string) {
        for (EventUserStateAction state: EventUserStateAction.values()) {
            if (state.name().equalsIgnoreCase(string))
                return Optional.of(state);
        }
        return Optional.empty();
    }
}
