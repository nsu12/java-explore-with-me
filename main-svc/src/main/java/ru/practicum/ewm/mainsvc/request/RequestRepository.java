package ru.practicum.ewm.mainsvc.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.mainsvc.request.dto.EventRequestStatus;
import ru.practicum.ewm.mainsvc.request.model.Request;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(value =
            "SELECT req.event.id AS eventId, COUNT(req.event) AS requestCount " +
            "FROM Request AS req " +
            "WHERE req.event.id IN :events AND req.status = 'CONFIRMED' " +
            "GROUP BY req.event.id"
    )
    List<RequestCountView> getConfirmedRequestCountViewsFor(List<Long> events);

    Integer countAllByEvent_IdAndStatusIs(Long eventId, EventRequestStatus status);

    List<Request> findAllByRequester_Id(Long userId);

    Optional<Request> findByIdAndRequester_Id(Long requestId, Long userId);

    List<Request> findAllByEvent_Id(Long eventId);

    List<Request> findAllByEvent_IdAndStatusIsAndIdIn(Long eventId, EventRequestStatus status, Collection<Long> id);

    Integer countAllByEvent_IdAndStatusNotAndIdIn(Long eventId, EventRequestStatus status, Collection<Long> id);
}
