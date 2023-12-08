package ru.practicum.ewm.mainsvc.error;

public class EntryNotFoundException extends RuntimeException {
    public EntryNotFoundException(String message) {
        super(message);
    }
}
