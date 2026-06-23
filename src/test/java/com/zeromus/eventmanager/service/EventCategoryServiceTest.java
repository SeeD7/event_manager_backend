package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.model.dto.EventCategoryDto;
import com.zeromus.eventmanager.model.entity.EventCategory;
import com.zeromus.eventmanager.model.mapper.EventCategoryMapper;
import com.zeromus.eventmanager.repository.EventCategoryRepository;
import com.zeromus.eventmanager.service.impl.EventCategoryService;
import com.zeromus.eventmanager.service.impl.UserService;
import com.zeromus.eventmanager.utils.AssertionUtils;
import com.zeromus.eventmanager.utils.UserUtils;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventCategoryServiceTest {

    private final EventCategory entity = EventCategory.builder().id(1L).name("Sport").build();
    private final EventCategoryDto expectedDto = EventCategoryDto.builder().id(1L).name("Sport").build();

    @Mock
    private EventCategoryRepository repository;
    @Mock
    private EventCategoryMapper eventCategoryMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private EventCategoryService service;

    @Test
    void getEventCategoryById_WhenIdIsOk_ShouldReturnEventCategory() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(eventCategoryMapper.toDto(entity)).thenReturn(expectedDto);
        EventCategoryDto result = service.getEventCategoryById(1L);
        Assertions.assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void getEventCategoryById_WhenIdIsUnknown_ShouldReturnException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(EntityNotFoundException.class, () -> service.getEventCategoryById(2L));

        AssertionUtils.assertExceptionMessageContains(ex, "EventCategory not found with ID: 2");
    }

    @Test
    void getAllEventCategory_ShouldCallRepository() {
        when(repository.findAll()).thenReturn(new PageImpl<>(Collections.singletonList(entity)));
        when(eventCategoryMapper.toDto(entity)).thenReturn(expectedDto);
        service.getAll();
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAllEventCategoryPages_ShouldCallRepository() {
        final Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(entity)));
        when(eventCategoryMapper.toDto(entity)).thenReturn(expectedDto);
        service.getAllEventCategoryPaged(pageable);
        verify(repository, times(1)).findAll((Pageable) any());
    }

    @Test
    void addEventCategory_ShouldReturnEventCategoryAndCallRepository() {
        EventCategoryDto newDto = EventCategoryDto.builder().name("Musique").creator("Lulu").build();
        when(eventCategoryMapper.toEntity(newDto)).thenReturn(entity);
        when(eventCategoryMapper.toDto(entity)).thenReturn(expectedDto);
        when(repository.save(entity)).thenReturn(entity);
        when(userService.getUserEntityByUsername("Lulu")).thenReturn(UserUtils.USER_ENTITY);
        EventCategoryDto result = service.addEventCategory(newDto);
        Assertions.assertThat(result).isEqualTo(expectedDto);
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateEventCategory_WhenIdIsOk_ShouldReturnUpdatedDtoAndCallRepository() {
        EventCategoryDto updatedDto = EventCategoryDto.builder().id(1L).name("Musique").lastUpdater("Lulu").build();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(userService.getUserEntityByUsername("Lulu")).thenReturn(UserUtils.USER_ENTITY);
        when(eventCategoryMapper.toDto(entity)).thenReturn(updatedDto);
        when(repository.save(any())).thenReturn(entity);
        service.updateEventCategory(1L, updatedDto);
        verify(repository, times(1)).save(any());
    }

    @Test
    void deleteEventCategory_WhenIdIsOk_ShouldCallRepository() {
        service.deleteEventCategory(1L);
        verify(repository, times(1)).deleteById(any());
    }
}
