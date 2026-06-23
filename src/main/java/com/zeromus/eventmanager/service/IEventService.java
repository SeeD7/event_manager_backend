package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.exceptions.EventNotPublishedException;
import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.enums.EventState;
import com.zeromus.eventmanager.model.search.SearchEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEventService {

    EventDto getEventById(Long id);

    EventDto getEventByEventName(String name);

    List<EventDto> getAllEventsSearched(SearchEvent search);

    Page<EventDto> getAllEventsPaged(SearchEvent search, Pageable pageable);

    EventDto addEvent(EventDto newDto);

    EventDto updateEvent(EventDto updateDto);

    Boolean addParticipant(Long idEvent, Long idUser) throws EventNotPublishedException;

    void removeParticipant(Long idEvent, Long idUser);

    EventDto changeState(Long idEvent, EventState state);
}
