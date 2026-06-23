package com.zeromus.eventmanager.model.entity;

import com.zeromus.eventmanager.model.enums.EventState;
import com.zeromus.eventmanager.model.validation.StartBeforeEndDateValid;
import com.zeromus.eventmanager.model.validation.StartEndDateable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "em_events")
@StartBeforeEndDateValid
@SuperBuilder
public class Event extends History implements StartEndDateable {
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

    @Column(name = "spots_available")
    private Long spotsAvailable;

    @ManyToMany
    @JoinTable(
            name = "em_participate",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> participants;

    public void addParticipant(User user) {
        participants.add(user);
    }

    public void removeParticipant(User user) {
        participants.remove(user);
    }
}
