package com.zeromus.eventmanager.model.dto;

import com.zeromus.eventmanager.model.enums.EventState;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFormDto {
    private Long id;

    @NotNull
    private String name;

    private String description;

    private String location;

    private Set<EventCategoryLightDto> category;

    private EventState state;

    private boolean allDay;

    @NotNull
    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

    private Long spotsAvailable;
}
