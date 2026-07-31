package com.zeromus.eventmanager.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "em_waiting_list")
@IdClass(WaitingListId.class)
@NoArgsConstructor
public class EventWaitingList {

    @Id
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "joined_at", insertable = false, updatable = false)
    private OffsetDateTime joinedAt;

    public EventWaitingList(Event event, User user) {
        this.event = event;
        this.user = user;
    }
}

