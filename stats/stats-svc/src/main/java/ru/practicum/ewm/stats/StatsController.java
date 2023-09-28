package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.EndpointHitInDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
public class StatsController {

    private final StatsService statsService;

    @PostMapping(value = "/hit")
    public EndpointHitDto addHit(@RequestBody @Valid EndpointHitInDto hitInDto) {
        return statsService.addHit(hitInDto);
    }

    @GetMapping(value = "/stats")
    public List<ViewStatsDto> getStats(
            @RequestParam(value = "start") String start,
            @RequestParam(value = "end") String end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") Boolean unique
    ) {
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (!endDate.isAfter(startDate)) {
            throw new ValidationException("end date must be after start date");
        }
        return statsService.getStats(startDate, endDate, uris, unique);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handleValidationException(ValidationException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    static class ErrorResponse {
        public final String error;

        public ErrorResponse(String message) {
            this.error = message;
        }

        public String getError() {
            return error;
        }
    }
}
