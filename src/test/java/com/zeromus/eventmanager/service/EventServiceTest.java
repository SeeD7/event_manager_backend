package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.exceptions.EventNotPublishedException;
import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.dto.EventFormDto;
import com.zeromus.eventmanager.model.entity.Event;
import com.zeromus.eventmanager.model.enums.EventState;
import com.zeromus.eventmanager.model.mapper.EventMapper;
import com.zeromus.eventmanager.model.mapper.UserMapper;
import com.zeromus.eventmanager.model.search.SearchEvent;
import com.zeromus.eventmanager.repository.EventRepository;
import com.zeromus.eventmanager.service.impl.EventCategoryService;
import com.zeromus.eventmanager.service.impl.EventService;
import com.zeromus.eventmanager.service.impl.UserService;
import com.zeromus.eventmanager.utils.AssertionUtils;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static com.zeromus.eventmanager.utils.EventUtils.*;
import static com.zeromus.eventmanager.utils.EventUtils.createValidTestEventFormDto;
import static com.zeromus.eventmanager.utils.UserUtils.USER_DTO;
import static com.zeromus.eventmanager.utils.UserUtils.USER_ENTITY;
import static org.junit.jupiter.api.Assertions.*;
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
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private EventCategoryService eventCategoryService;
    @Mock
    private Authentication auth;
    @InjectMocks
    private EventService service;


    @Test
    void getEventById_WhenIdIsOk_ShouldReturnEvent() {
        setUpAuthenticationMock();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(expectedDto);
        EventDto result = service.getEventById(1L);
        Assertions.assertThat(result).isEqualTo(expectedDto);

        SecurityContextHolder.clearContext();
    }

    @Test
    void getEventById_WhenIdIsUnknown_ShouldReturnException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(EntityNotFoundException.class, () -> service.getEventById(2L));

        AssertionUtils.assertExceptionMessageContains(ex, "Event not found with ID: 2");
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
        service.getAllEventsPaged(search, pageable);
        verify(repository, times(1)).findAll(any(), (Pageable) any());
    }

    @Test
    void addEvent_ShouldReturnEventAndCallRepository() {
        setUpAuthenticationMock();

        EventFormDto newDto = createValidTestEventFormDto();
        newDto.setId(null);
        newDto.setName("Un petit nom");
        when(mapper.toEntity(newDto)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(expectedDto);
        when(repository.save(entity)).thenReturn(entity);
        EventDto result = service.addEvent(newDto);
        Assertions.assertThat(result).isEqualTo(expectedDto);
        verify(repository, times(1)).save(any());

        SecurityContextHolder.clearContext();
    }

    @Test
    void updateEvent_WhenIdIsOk_ShouldReturnUpdatedDtoAndCallRepository() {
        setUpAuthenticationMock();

        // Instance isolée, aucune pollution du test précédent !
        Event localEntity = createValidTestEvent();
        EventDto localExpectedDto = createValidTestEventDto();
        EventFormDto updatedDto = createValidTestEventFormDto();
        updatedDto.setName("Un petit nom");

        when(repository.findById(1L)).thenReturn(Optional.of(localEntity));
        when(repository.save(any())).thenReturn(localEntity); // Requis par ton mapper.toDto(repository.save)
        when(eventCategoryService.getEventCategoryEntityById(any())).thenReturn(CATEGORY_PERSO);
        when(mapper.toDto(any())).thenReturn(localExpectedDto); // Requis pour le return du service

        service.updateEvent(updatedDto);
        verify(repository, times(1)).save(any());

        SecurityContextHolder.clearContext();
    }

    @Test
    void updateEvent_WhenIdIsOkAndNothingChanged_ShouldReturnUpdatedDtoAndCallRepository() {
        setUpAuthenticationMock();

        Event localEntity = createValidTestEvent();
        EventDto localExpectedDto = createValidTestEventDto();
        EventFormDto updatedDto = createValidTestEventFormDto();

        when(repository.findById(any())).thenReturn(Optional.of(localEntity));
        when(repository.save(any())).thenReturn(localEntity);
        when(eventCategoryService.getEventCategoryEntityById(any())).thenReturn(CATEGORY_PERSO);
        when(mapper.toDto(any())).thenReturn(localExpectedDto);

        service.updateEvent(updatedDto);
        verify(repository, times(1)).save(any());

        SecurityContextHolder.clearContext();
    }

    @Test
    void participateEvent_WhenNoSpotAvailableDefined_ShouldCallRepository() throws EventNotPublishedException {
        when(userService.getUserById(1L)).thenReturn(USER_DTO);
        when(userMapper.toEntity(USER_DTO)).thenReturn(USER_ENTITY);
        entity.setSpotsAvailable(null);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        service.addParticipant(1L,1L);
        verify(repository, times(1)).save(any());
    }

    @Test
    void participateEvent_WhenSpotAvailable_ShouldCallRepository() throws EventNotPublishedException {
        when(userService.getUserById(1L)).thenReturn(USER_DTO);
        when(userMapper.toEntity(USER_DTO)).thenReturn(USER_ENTITY);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        service.addParticipant(1L,1L);
        verify(repository, times(1)).save(any());
    }

    @Test
    void participateEvent_WhenNoSpotAvailable_ShouldCallRepository() throws EventNotPublishedException {
        when(userService.getUserById(1L)).thenReturn(USER_DTO);
        when(userMapper.toEntity(USER_DTO)).thenReturn(USER_ENTITY);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        entity.addParticipant(USER_ENTITY);
        service.addParticipant(1L,1L);
        verify(repository, times(1)).save(any());
    }

    @Test
    void participateEvent_WhenEventNotPublished_ThrowsException() {
        entity.setState(EventState.DRAFT);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        Exception ex = assertThrows(EventNotPublishedException.class, () -> service.addParticipant(1L,1L));

        AssertionUtils.assertExceptionMessageContains(ex, "L'évènement n'est pas publié");
    }

    @Test
    void participateEvent_WhenEventDoesntExist_ThrowsException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(EntityNotFoundException.class, () -> service.addParticipant(1L,1L));

        AssertionUtils.assertExceptionMessageContains(ex, "Event not found with ID: " + 1L);
    }

    @Test
    void cancelEvent_ShouldCallRepository() {
        when(userService.getUserById(1L)).thenReturn(USER_DTO);
        when(userMapper.toEntity(USER_DTO)).thenReturn(USER_ENTITY);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        service.removeParticipant(1L, 1L);
        verify(repository, times(1)).save(any());
    }

    @Test
    void cancelEvent_WhenEventDoesntExist_ThrowsException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(EntityNotFoundException.class, () -> service.removeParticipant(1L,1L));

        AssertionUtils.assertExceptionMessageContains(ex, "Event not found with ID: " + 1L);
    }

    @Test
    void changeState_ShouldCallRepository() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        int i =0;
        for(EventState event : EventState.values()) {
            service.changeState(1L, event);
            verify(repository, times(++i)).save(any());
        }
    }

    @Test
    void changeState_WhenEventDoesntExist_ThrowsException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        for(EventState event : EventState.values()) {
            Exception ex = assertThrows(EntityNotFoundException.class, () -> service.changeState(1L, event));

            AssertionUtils.assertExceptionMessageContains(ex, "Event not found with ID: " + 1L);
        }
    }

    private void setUpAuthenticationMock() {
        SecurityContext securityContext = org.mockito.Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }
}
