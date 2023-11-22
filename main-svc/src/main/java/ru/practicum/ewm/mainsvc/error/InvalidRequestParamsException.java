package ru.practicum.ewm.mainsvc.error;

public class InvalidRequestParamsException extends RuntimeException {
    public InvalidRequestParamsException(String message) {
        super(message);
    }
}
