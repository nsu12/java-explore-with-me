package ru.practicum.ewm.stats;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query(value =
            "SELECT eh.app AS app, eh.uri AS uri, COUNT(eh.uri) AS hitCount " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY hitCount DESC"
    )
    List<HitCountView> getHitCountFromDates(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value =
            "SELECT sub.app AS app, sub.uri AS uri, COUNT(sub.uri) as hitCount " +
            "FROM ( " +
            "   SELECT eh.app, eh.uri, eh.ip " +
            "   FROM endpoint_hit AS eh " +
            "   WHERE eh.ts BETWEEN ?1 AND ?2 " +
            "   GROUP BY eh.app, eh.uri, eh.ip " +
            ") AS sub " +
            "GROUP BY sub.app, sub.uri " +
            "ORDER BY hitCount DESC",
            nativeQuery = true
    )
    List<HitCountView> getHitCountFromDatesUnique(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value =
            "SELECT eh.app AS app, eh.uri AS uri, COUNT(eh.uri) AS hitCount " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 AND eh.uri IN (?3) " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY hitCount DESC"
    )
    List<HitCountView> getHitCountFromDatesFiltered(LocalDateTime startDate,
                                                    LocalDateTime endData, List<String> uris);

    @Query(value =
            "SELECT sub.app AS app, sub.uri AS uri, COUNT(sub.uri) as hitCount " +
            "FROM ( " +
            "   SELECT eh.app, eh.uri, eh.ip " +
            "   FROM endpoint_hit AS eh " +
            "   WHERE eh.ts BETWEEN ?1 AND ?2 AND eh.uri IN (?3) " +
            "   GROUP BY eh.app, eh.uri, eh.ip " +
            ") AS sub " +
            "GROUP BY sub.app, sub.uri " +
            "ORDER BY hitCount DESC",
            nativeQuery = true
    )
    List<HitCountView> getHitCountFromDatesFilteredUnique(LocalDateTime startDate,
                                                          LocalDateTime endData, List<String> uris);

}
