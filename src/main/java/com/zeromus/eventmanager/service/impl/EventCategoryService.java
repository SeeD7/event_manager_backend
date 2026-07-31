package com.zeromus.eventmanager.service.impl;

import com.zeromus.eventmanager.model.dto.EventCategoryDto;
import com.zeromus.eventmanager.model.dto.EventCategoryLightDto;
import com.zeromus.eventmanager.model.entity.EventCategory;
import com.zeromus.eventmanager.model.entity.User;
import com.zeromus.eventmanager.model.mapper.EventCategoryMapper;
import com.zeromus.eventmanager.repository.EventCategoryRepository;
import com.zeromus.eventmanager.service.IEventCategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service("eventCategoryService")
@Transactional
public class EventCategoryService implements IEventCategoryService {

    public static final String EVENT_CATEGORY_NOT_FOUND_WITH_ID = "EventCategory not found with ID: ";
    private final EventCategoryRepository repository;
    private final EventCategoryMapper mapper;
    private final UserService userService;

    public EventCategoryService(final EventCategoryRepository repository, final EventCategoryMapper mapper, final UserService userService) {
        this.repository = repository;
        this.mapper = mapper;
        this.userService = userService;
    }

    @Override
    public EventCategoryDto getEventCategoryById(Long id) {
        Optional<EventCategory> eventCategory = repository.findById(id);
        return eventCategory.map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_CATEGORY_NOT_FOUND_WITH_ID + id));
    }

    @Override
    public EventCategory getEventCategoryEntityById(Long id) {
        Optional<EventCategory> eventCategory = repository.findById(id);
        return eventCategory.orElseThrow(() -> new EntityNotFoundException(EVENT_CATEGORY_NOT_FOUND_WITH_ID + id));
    }

    @Override
    public Optional<EventCategoryDto> getEventCategoryByName(String name) {
        return repository.findByName(name).map(mapper::toDto);
    }

    public List<EventCategoryLightDto> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).map(mapper::toLightDto).toList();
    }

    public Page<EventCategoryDto> getAllEventCategoryPaged(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public EventCategoryDto addEventCategory(EventCategoryDto newDto) {
        User user = userService.getUserEntityByUsername(newDto.getCreator());
        EventCategory entity = mapper.toEntity(newDto);
        entity.setCreator(user);
        entity.setCreatedDate(OffsetDateTime.now(ZoneId.systemDefault()));
        return mapper.toDto(repository.save(entity));
    }

    public EventCategoryDto updateEventCategory(Long id, EventCategoryDto updatedDto) {
        Optional<EventCategory> optEvent = repository.findById(id);
        return optEvent.map(event -> {
            User user = userService.getUserEntityByUsername(updatedDto.getLastUpdater());
            event.setLastUpdater(user);
            event.setLastUpdatedDate(OffsetDateTime.now(ZoneId.systemDefault()));
            event.setName(updatedDto.getName());
            event.setIcon(updatedDto.getIcon());
            return mapper.toDto(repository.save(event));
        }).orElseThrow(() -> new EntityNotFoundException(EVENT_CATEGORY_NOT_FOUND_WITH_ID + id));
    }

    public void deleteEventCategory(Long id) {
        repository.deleteById(id);
    }
}
