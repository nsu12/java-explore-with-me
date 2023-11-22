package ru.practicum.ewm.mainsvc.request.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.mainsvc.event.model.Event;
import ru.practicum.ewm.mainsvc.request.dto.EventRequestStatus;
import ru.practicum.ewm.mainsvc.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "participation_request", schema = "public")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EventRequestStatus status;
}
