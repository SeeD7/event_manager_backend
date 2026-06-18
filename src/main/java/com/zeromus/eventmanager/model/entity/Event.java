package com.zeromus.eventmanager.model.entity;

import com.zeromus.eventmanager.model.enums.EventState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "em_events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToMany
    @JoinTable(
        name = "em_event_category_join",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "event_category_id"))
    private Set<EventCategory> category;

    @Column
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "all_day")
    private boolean allDay;

    @Column(name = "start_date")
    private OffsetDateTime startDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;
}
