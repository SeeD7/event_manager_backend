package com.zeromus.eventmanager.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryDto {

    private Long id;

    @NotNull
    private String name;
}
