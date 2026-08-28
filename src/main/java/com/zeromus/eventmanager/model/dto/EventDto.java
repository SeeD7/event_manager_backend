package com.zeromus.eventmanager.model.dto;

import com.zeromus.eventmanager.model.entity.EventCategory;
import com.zeromus.eventmanager.model.enums.EventState;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EventDto extends HistoryDto {
    private Long id;

    @NotNull
    private String name;

    private String description;

    private String location;

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

    private List<UserDto> waitingList;

    private Boolean isCurrentUserRegistered;

    private Boolean isCurrentUserInWaitingList;
}
