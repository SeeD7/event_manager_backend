package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.entity.Event;
import com.zeromus.eventmanager.model.mapper.EventMapper;
import com.zeromus.eventmanager.model.search.SearchEvent;
import com.zeromus.eventmanager.repository.EventRepository;
import com.zeromus.eventmanager.utils.AssertionUtils;
import com.zeromus.eventmanager.utils.EventUtils;
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

import static com.zeromus.eventmanager.utils.EventUtils.createValidTestEvent;
import static com.zeromus.eventmanager.utils.EventUtils.createValidTestEventDto;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    private final Event entity = createValidTestEvent();
    private final EventDto expectedDto = createValidTestEventDto();

    private final SearchEvent search = new SearchEvent();
    private final Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));

    @Mock
    private EventRepository repository;
    @Mock
    private EventMapper mapper;
    @InjectMocks
    private EventService service;

    @Test
    void getEventById_WhenIdIsOk_ShouldReturnEvent() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(expectedDto);
        EventDto result = service.getEventById(1L);
        Assertions.assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void getEventById_WhenIdIsUnknown_ShouldReturnException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(EntityNotFoundException.class, () -> service.getEventById(2L));

        AssertionUtils.assertExceptionMessageContains(ex, "Event not found with ID: 2");
    }

    @Test
    void getEventByEventName_WhenEventNameIsOk_ShouldReturnEvent() {
        when(repository.findByName(EventUtils.NAME)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(expectedDto);
        EventDto result = service.getEventByEventName(EventUtils.NAME);
        Assertions.assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void getEventByEventName_WhenEventNameIsUnknown_ShouldReturnException() {
        when(repository.findByName(EventUtils.NAME)).thenReturn(Optional.empty());
        Exception ex = assertThrows(EntityNotFoundException.class, () -> service.getEventByEventName(EventUtils.NAME));

        AssertionUtils.assertExceptionMessageContains(ex, "Event not found with name: " + EventUtils.NAME);
    }

    @Test
    void getAllEventsSearched_ShouldCallRepository() {
        when(repository.findAll(search)).thenReturn(Collections.singletonList(entity));
        when(mapper.toDto(entity)).thenReturn(expectedDto);
        service.getAllEventsSearched(search);
        verify(repository, times(1)).findAll(search);
    }

    @Test
    void getAllEventsPaged_ShouldCallRepository() {
        when(repository.findAll(search, pageable)).thenReturn(new PageImpl<>(Collections.singletonList(entity)));
        when(mapper.toDto(entity)).thenReturn(expectedDto);
        service.getAllEventsPaged(search,pageable);
        verify(repository, times(1)).findAll(any(), (Pageable) any());
    }

    @Test
    void addEvent_ShouldReturnEventAndCallRepository() {
        EventDto newDto = createValidTestEventDto();
        newDto.setId(null);
        newDto.setName("Un petit nom");
        when(mapper.toEntity(newDto)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(expectedDto);
        when(repository.save(entity)).thenReturn(entity);
        EventDto result = service.addEvent(newDto);
        Assertions.assertThat(result).isEqualTo(expectedDto);
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateEvent_WhenIdIsOk_ShouldReturnUpdatedDtoAndCallRepository() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        EventDto updatedDto = createValidTestEventDto();
        updatedDto.setName("Un petit nom");
        service.updateEvent(1L, updatedDto);
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateEvent_WhenIdIsOkAndNothingChanged_ShouldReturnUpdatedDtoAndCallRepository() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        service.updateEvent(1L, expectedDto);
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateEvent_WhenIdIsUnknown_ShouldReturnException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(EntityNotFoundException.class, () -> service.updateEvent(2L, expectedDto));

        AssertionUtils.assertExceptionMessageContains(ex, "Event not found with id: 2");
    }

    @Test
    void deleteEvent_WhenIdIsOk_ShouldCallRepository() {
        service.deleteEvent(1L);
        verify(repository, times(1)).deleteById(any());
    }

    @Test
    void deleteEvent_WhenIdIsUnknown_ShouldCallRepository() {
        service.deleteEvent(2L);
        verify(repository, times(1)).deleteById(any());
    }
}
