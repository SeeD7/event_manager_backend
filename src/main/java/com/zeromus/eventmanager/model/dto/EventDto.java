package com.zeromus.eventmanager.model.dto;

import com.zeromus.eventmanager.model.entity.EventCategory;
import com.zeromus.eventmanager.model.enums.EventState;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Set<EventCategory> category;

    private EventState state;

    private boolean allDay;

    @NotNull
    private OffsetDateTime startDate;

    @NotNull
    private OffsetDateTime endDate;
}
