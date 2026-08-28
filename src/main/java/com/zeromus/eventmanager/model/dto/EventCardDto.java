package com.zeromus.eventmanager.model.dto;

import com.zeromus.eventmanager.model.entity.EventCategory;
import com.zeromus.eventmanager.model.enums.EventState;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@EqualsAndHashCode
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class EventCardDto {
    private final Long id;

    @NotNull
    private final String name;

    @NotNull
    private Set<EventCategory> category;

    private final EventState state;

    private final boolean allDay;

    @NotNull
    private final OffsetDateTime startDate;

    @NotNull
    private final OffsetDateTime endDate;

    private final Long spotsAvailable;

    private final Integer participants;

    private final Integer waitingList;
}