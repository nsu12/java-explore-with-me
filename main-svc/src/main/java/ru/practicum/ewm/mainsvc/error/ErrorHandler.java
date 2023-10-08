package ru.practicum.ewm.mainsvc.error;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorMessage handleValidationException(ValidationException exception) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                exception.getMessage(),
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorMessage handleConstraintViolationException(ConstraintViolationException exception) {
        return new ErrorMessage(
                HttpStatus.CONFLICT,
                "Integrity constraint has been violated.",
                exception.getMessage(),
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntryNotFoundException.class)
    public ErrorMessage handleEntryNotFoundException(EntryNotFoundException exception) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND,
                "The required object was not found.",
                exception.getMessage(),
                LocalDateTime.now()
        );
    }
}
