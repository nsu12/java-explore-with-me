package ru.practicum.ewm.mainsvc.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorMessage {
    private final HttpStatus status;
    private final String reason;
    private final String message;
    private final LocalDateTime timestamp;
}
