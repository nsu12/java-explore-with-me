package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.EndpointHitInDto;
import ru.practicum.ewm.stats.dto.EndpointHitMapper;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Transactional
    @Override
    public EndpointHitDto addHit(EndpointHitInDto hitInDto) {
        EndpointHit hit = new EndpointHit();
        hit.setApp(hitInDto.getApp());
        hit.setUri(hitInDto.getUri());
        hit.setIp(hitInDto.getIp());
        hit.setTimestamp(hitInDto.getTimestamp());
        return EndpointHitMapper.toDto(statsRepository.save(hit));
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime startDate,
                                       LocalDateTime endDate,
                                       List<String> uris,
                                       Boolean unique) {
        log.debug("Stats request from {} to {}, unique {}", startDate, endDate, unique);

        List<HitCountView> hits;
        if (uris != null && !uris.isEmpty()) {
            if (unique) {
                hits = statsRepository.getHitCountFromDatesFilteredUnique(startDate, endDate, uris);
            } else {
                hits = statsRepository.getHitCountFromDatesFiltered(startDate, endDate, uris);
            }
        } else {
            if (unique) {
                hits = statsRepository.getHitCountFromDatesUnique(startDate, endDate);
            } else {
                hits = statsRepository.getHitCountFromDates(startDate, endDate);
            }
        }
        return hits.stream()
                .map(hitCount -> new ViewStatsDto(hitCount.getApp(), hitCount.getUri(), hitCount.getHitCount()))
                .collect(Collectors.toList());
    }
}
