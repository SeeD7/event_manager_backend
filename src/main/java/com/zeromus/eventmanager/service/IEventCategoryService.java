package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.model.dto.EventCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IEventCategoryService {


    EventCategoryDto getEventCategoryById(Long id);

    Optional<EventCategoryDto> getEventCategoryByName(String name);

    List<EventCategoryDto> getAll();

    Page<EventCategoryDto> getAllEventCategoryPaged(Pageable pageable);

    EventCategoryDto addEventCategory(EventCategoryDto newDto);

    EventCategoryDto updateEventCategory(Long id, EventCategoryDto updatedDto);

    void deleteEventCategory(Long id);
}
