package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.entity.Event;
import com.zeromus.eventmanager.model.mapper.EventMapper;
import com.zeromus.eventmanager.model.search.SearchEvent;
import com.zeromus.eventmanager.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService {

    private final EventRepository repository;
    private final EventMapper mapper;

    public EventService(final EventRepository repository, final EventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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

    public EventDto updateEvent(Long id, EventDto updateDto) {
        Optional<Event> e = repository.findById(id);
        if (e.isPresent()) {
            updateDto.setId(e.get().getId());
            repository.save(mapper.toEntity(updateDto));
            return updateDto;
        } else {
            throw new EntityNotFoundException("Event not found with id: " + id);
        }
    }

    public void deleteEvent(Long id) {
        repository.deleteById(id);
    }
}
