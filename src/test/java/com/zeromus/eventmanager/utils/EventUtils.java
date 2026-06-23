package com.zeromus.eventmanager.utils;

import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.entity.Event;
import com.zeromus.eventmanager.model.entity.EventCategory;
import com.zeromus.eventmanager.model.enums.EventState;

import java.util.ArrayList;
import java.util.Set;

import static java.time.OffsetDateTime.parse;

public class EventUtils {
    public static final String NAME = "Séance de papouilles & Câlin avec ma chérie";
    public static final String DESC = "Session obligatoire de détente avec les minets, Baphomette, et un gros câlin pour recharger les batteries.";
    public static final EventCategory CATEGORY_PERSO = EventCategory.builder().id(1L).name("Perso").build();

    private EventUtils() {
    }

    public static Event createValidTestEvent() {
        return Event.builder().id(1L).name(NAME).description(DESC).category(Set.of(CATEGORY_PERSO))
                .state(EventState.PUBLISHED).allDay(false).startDate(parse("2026-06-17T18:30:00+02:00"))
                .endDate(parse("2026-06-17T20:00:00+02:00")).spotsAvailable(1L).participants(new ArrayList<>())
                .build();
    }

    public static EventDto createValidTestEventDto() {
        return EventDto.builder().id(1L).name(NAME).description(DESC).category(Set.of(CATEGORY_PERSO))
                .state(EventState.PUBLISHED).allDay(false).startDate(parse("2026-06-17T18:30:00+02:00"))
                .endDate(parse("2026-06-17T20:00:00+02:00")).spotsAvailable(1L).participants(new ArrayList<>())
                .build();
    }
}
