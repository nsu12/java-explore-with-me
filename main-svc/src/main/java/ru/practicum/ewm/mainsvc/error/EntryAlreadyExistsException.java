package ru.practicum.ewm.mainsvc.error;

public class EntryAlreadyExistsException extends RuntimeException {
    public EntryAlreadyExistsException(String message) {
        super(message);
    }
}
