package ru.practicum.ewm.mainsvc.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ApiError handleValidationException(ValidationException exception) {
        return new ApiError(
                HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                exception.getMessage(),
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        return new ApiError(
                HttpStatus.CONFLICT,
                "Integrity constraint has been violated.",
                exception.getMessage(),
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntryNotFoundException.class)
    public ApiError handleEntryNotFoundException(EntryNotFoundException exception) {
        return new ApiError(
                HttpStatus.NOT_FOUND,
                "The required object was not found.",
                exception.getMessage(),
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(InvalidRequestParamsException.class)
    public ApiError handleInvalidRequestParamsException(InvalidRequestParamsException exception) {
        return new ApiError(
                HttpStatus.CONFLICT,
                "For the requested operation the conditions are not met.",
                exception.getMessage(),
                LocalDateTime.now()
        );
    }
}
