package com.zeromus.eventmanager.model.dto;

import com.zeromus.eventmanager.model.entity.EventCategory;
import com.zeromus.eventmanager.model.enums.EventState;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long id;

    @NotNull
    private String name;

    private String description;

    private String location;

    @NotNull
    private UserDto creator;

    @NotNull
    private Set<EventCategory> category;

    private EventState state;

    private boolean allDay;

    @NotNull
    private OffsetDateTime startDate;

    @NotNull
    private OffsetDateTime endDate;

    private Long spotsAvailable;

    private List<UserDto> participants;

    @NotNull
    private OffsetDateTime createdDate;

    private OffsetDateTime lastUpdatedDate;
}
