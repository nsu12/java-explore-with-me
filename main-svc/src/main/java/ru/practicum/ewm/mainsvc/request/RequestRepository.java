package ru.practicum.ewm.mainsvc.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.mainsvc.request.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(value =
            "SELECT req.event.id AS eventId, COUNT(req.event) AS requestCount " +
            "FROM Request AS req " +
            "WHERE req.event.id IN :events " +
            "GROUP BY req.event.id"
    )
    List<RequestCountView> getRequestCountFor(List<Long> events);
}
