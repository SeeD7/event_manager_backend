package com.zeromus.eventmanager.model.validation;

import java.time.OffsetDateTime;

public interface StartEndDateable {
    OffsetDateTime getStartDate();

    OffsetDateTime getEndDate();
}