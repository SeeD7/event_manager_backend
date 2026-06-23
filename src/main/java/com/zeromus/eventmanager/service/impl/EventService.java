package com.zeromus.eventmanager.service.impl;

import com.zeromus.eventmanager.exceptions.EventNotPublishedException;
import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.entity.Event;
import com.zeromus.eventmanager.model.entity.User;
import com.zeromus.eventmanager.model.enums.EventState;
import com.zeromus.eventmanager.model.mapper.EventMapper;
import com.zeromus.eventmanager.model.mapper.UserMapper;
import com.zeromus.eventmanager.model.search.SearchEvent;
import com.zeromus.eventmanager.repository.EventRepository;
import com.zeromus.eventmanager.service.IEventService;
import com.zeromus.eventmanager.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("eventService")
@Transactional
public class EventService implements IEventService {

    private final EventRepository repository;
    private final EventMapper mapper;
    private final IUserService userService;
    private final UserMapper userMapper;

    public EventService(final EventRepository repository, final EventMapper mapper, final IUserService userService, final UserMapper userMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public EventDto getEventById(Long id) {
        Optional<Event> eventCategory = repository.findById(id);
        return eventCategory.map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));
    }

    public EventDto getEventByEventName(String name) {
        Optional<Event> user = repository.findByName(name);
        return user.map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with name: " + name));
    }

    public List<EventDto> getAllEventsSearched(SearchEvent search) {
        return repository.findAll(search).stream().map(mapper::toDto).toList();
    }

    public Page<EventDto> getAllEventsPaged(SearchEvent search, Pageable pageable) {
        return repository.findAll(search, pageable).map(mapper::toDto);
    }

    public EventDto addEvent(EventDto newDto) {
        return mapper.toDto(repository.save(mapper.toEntity(newDto)));
    }

    public EventDto updateEvent(EventDto updateDto) {
            return mapper.toDto(repository.save(mapper.toEntity(updateDto)));
    }

    public Boolean addParticipant(Long idEvent, Long idUser) throws EventNotPublishedException {
        Optional<Event> optEvent = repository.findById(idEvent);
        if (optEvent.isPresent()) {
            Event event = optEvent.get();
            if(!event.getState().equals(EventState.PUBLISHED)){
                throw new EventNotPublishedException();
            }
            if(!Objects.isNull(event.getSpotsAvailable()) && event.getParticipants().size()>= event.getSpotsAvailable()){
                return false;
            }
            User user = userMapper.toEntity(userService.getUserById(idUser));
            event.addParticipant(user);
            repository.save(event);
            return true;
        } else {
            throw new EntityNotFoundException("Event not found with ID: " + idEvent);
        }
    }

    public void removeParticipant(Long idEvent, Long idUser) {
        Optional<Event> optEvent = repository.findById(idEvent);
        if (optEvent.isPresent()) {
            Event event = optEvent.get();
            User user = userMapper.toEntity(userService.getUserById(idUser));
            event.removeParticipant(user);
            repository.save(event);
        } else {
            throw new EntityNotFoundException("Event not found with ID: " + idEvent);
        }
    }

    @Override
    public EventDto changeState(Long idEvent, EventState state) {
        Optional<Event> optEvent = repository.findById(idEvent);
        if (optEvent.isPresent()) {
            Event event = optEvent.get();
            event.setState(state);
            return mapper.toDto(repository.save(event));
        } else {
            throw new EntityNotFoundException("Event not found with ID: " + idEvent);
        }
    }
}
