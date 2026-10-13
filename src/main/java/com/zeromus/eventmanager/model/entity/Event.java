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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    @Column
    private String location;

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
    private List<User> participants = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventWaitingList> waitingList = new ArrayList<>();

    public void addParticipant(User user) {
        participants.add(user);
    }

    public void removeParticipant(User user) {
        participants.remove(user);
    }

    public void addInWaintingList(User user) {
        waitingList.add(new EventWaitingList(this, user));
    }

    public void removeFromWaitingList(User user) {
        waitingList.removeIf(x -> x.getEvent().equals(this) && x.getUser().equals(user));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
