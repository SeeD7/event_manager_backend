package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.exceptions.EventNotPublishedException;
import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.dto.EventFormDto;
import com.zeromus.eventmanager.model.enums.EventState;
import com.zeromus.eventmanager.model.search.SearchEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEventService {

    EventDto getEventById(Long id);

    List<EventDto> getAllEventsSearched(SearchEvent search);

    Page<EventDto> getAllEventsPaged(SearchEvent search, Pageable pageable);

    EventDto addEvent(EventFormDto newDto);

    EventDto updateEvent(EventFormDto updateDto);

    void addParticipant(Long idEvent, Long idUser) throws EventNotPublishedException;

    void removeParticipant(Long idEvent, Long idUser);

    EventDto changeState(Long idEvent, EventState state);
}
