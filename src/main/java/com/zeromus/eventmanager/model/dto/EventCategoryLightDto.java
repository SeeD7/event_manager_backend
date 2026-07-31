package com.zeromus.eventmanager.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryLightDto {

    private Long id;

    @NotNull
    private String name;

    private String icon;
}
