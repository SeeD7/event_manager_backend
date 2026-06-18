package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.model.dto.EventCategoryDto;
import com.zeromus.eventmanager.model.entity.EventCategory;
import com.zeromus.eventmanager.model.mapper.EventCategoryMapper;
import com.zeromus.eventmanager.repository.EventCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.*;

@Service
@Transactional
public class EventCategoryService {

    private final EventCategoryRepository repository;
    private final EventCategoryMapper mapper;

    public EventCategoryService(final EventCategoryRepository repository, final EventCategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public EventCategoryDto getEventCategoryById(Long id) {
        Optional<EventCategory> eventCategory = repository.findById(id);
        return eventCategory.map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("EventCategory not found with ID: " + id));
    }

    public List<EventCategoryDto> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).map(mapper::toDto).collect(toList());
    }

    public Page<EventCategoryDto> getAllEventCategoryPaged(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public EventCategoryDto addEventCategory(EventCategoryDto newDto) {
        return mapper.toDto(repository.save(mapper.toEntity(newDto)));
    }

    public EventCategoryDto updateEventCategory(long l, EventCategoryDto updatedDto) {
        return mapper.toDto(repository.save(mapper.toEntity(updatedDto)));
    }

    public void deleteEventCategory(Long id) {
        repository.deleteById(id);
    }
}
