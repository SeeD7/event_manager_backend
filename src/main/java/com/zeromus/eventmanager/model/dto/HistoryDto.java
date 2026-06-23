package com.zeromus.eventmanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HistoryDto {
    private OffsetDateTime createdDate;

    private String creator;

    private OffsetDateTime lastUpdatedDate;

    private String lastUpdater;
}
