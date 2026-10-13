package com.zeromus.eventmanager.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class History {
    @Column(name = "created_date")
    private OffsetDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "creator")
    private User creator;

    @Column(name = "last_updated_date")
    private OffsetDateTime lastUpdatedDate;

    @ManyToOne
    @JoinColumn(name = "last_updater")
    private User lastUpdater;
}
