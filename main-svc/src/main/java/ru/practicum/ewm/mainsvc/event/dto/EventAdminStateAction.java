package ru.practicum.ewm.mainsvc.event.dto;

import java.util.Optional;

public enum EventAdminStateAction {
    PUBLISH_EVENT,
    REJECT_EVENT;

    public static Optional<EventAdminStateAction> fromString(String string) {
        for (EventAdminStateAction state: EventAdminStateAction.values()) {
            if (state.name().equalsIgnoreCase(string))
                return Optional.of(state);
        }
        return Optional.empty();
    }
}
