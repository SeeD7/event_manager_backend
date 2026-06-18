package com.zeromus.eventmanager.utils;

import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.entity.Event;
import com.zeromus.eventmanager.model.entity.EventCategory;
import com.zeromus.eventmanager.model.enums.EventState;

import java.util.Set;

import static java.time.OffsetDateTime.parse;

public class EventUtils {
    private EventUtils() {}

    public static final String NAME = "Séance de papouilles & Câlin avec ma chérie";
    public static final String DESC = "Session obligatoire de détente avec les minets, Baphomette, et un gros câlin pour recharger les batteries.";

    private static final EventCategory CATEGORY_PERSO = new EventCategory(1L, "Perso");


    public static Event createValidTestEvent() {
        return new Event(1L, NAME, DESC, Set.of(CATEGORY_PERSO), EventState.PUBLISHED, false, parse("2026-06-17T18:30:00+02:00"), parse("2026-06-17T20:00:00+02:00"));
    }

    public static EventDto createValidTestEventDto() {
        return new EventDto(1L, NAME, DESC, Set.of(CATEGORY_PERSO), EventState.PUBLISHED, false, parse("2026-06-17T18:30:00+02:00"), parse("2026-06-17T20:00:00+02:00"));
    }
}
