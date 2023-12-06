package ru.practicum.ewm.mainsvc.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.mainsvc.event.dto.EventState;
import ru.practicum.ewm.mainsvc.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiator_Id(Long userId, PageRequest pageRequest);

    Optional<Event> findByIdAndInitiator_Id(Long eventId, Long userId);

    Optional<Event> findByIdAndState(Long eventId, EventState eventState);

    @Query( "SELECT e FROM Event e " +
            "WHERE  (:users IS NULL OR e.initiator.id IN :users) " +
            "   AND (:states IS NULL OR e.state IN :states) " +
            "   AND (:categories IS NULL OR e.category.id IN :categories) " +
            "   AND (COALESCE(:rangeStart, null) IS NULL OR e.eventDate >= :rangeStart) " +
            "   AND (COALESCE(:rangeEnd, null) IS NULL OR e.eventDate <= :rangeEnd)"
    )
    Page<Event> findAllBy(List<Long> users,
                          List<EventState> states,
                          List<Long> categories,
                          LocalDateTime rangeStart,
                          LocalDateTime rangeEnd,
                          PageRequest pageRequest);

    @Query( "SELECT e FROM Event e " +
            "WHERE  e.state = 'PUBLISHED' " +
            "   AND (:text IS NULL OR (upper(e.annotation) LIKE upper(concat('%', :text, '%'))) " +
            "                      OR (upper(e.description) LIKE upper(concat('%', :text, '%')))) " +
            "   AND (:categories IS NULL OR e.category.id IN :categories)" +
            "   AND (:paid IS NULL OR e.paid = :paid) " +
            "   AND (COALESCE(:rangeStart, null) IS NULL OR e.eventDate >= :rangeStart) " +
            "   AND (COALESCE(:rangeEnd, null) IS NULL OR e.eventDate <= :rangeEnd) "
    )
    Page<Event> findPublishedEventsBy(String text,
                                      List<Long> categories,
                                      Boolean paid,
                                      LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd,
                                      PageRequest pageRequest);
}
