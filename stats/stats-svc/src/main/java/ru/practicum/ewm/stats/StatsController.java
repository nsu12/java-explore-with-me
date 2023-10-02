package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
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
}
