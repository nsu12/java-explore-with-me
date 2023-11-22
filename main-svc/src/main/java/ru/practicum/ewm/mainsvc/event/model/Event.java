package ru.practicum.ewm.mainsvc.event.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.mainsvc.category.model.Category;
import ru.practicum.ewm.mainsvc.event.dto.EventState;
import ru.practicum.ewm.mainsvc.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "event", schema = "public")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String annotation;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @JoinColumn(name = "location_lat")
    private Float locationLat;
    @JoinColumn(name = "location_lon")
    private Float locationLon;
    private boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventState state;
}
