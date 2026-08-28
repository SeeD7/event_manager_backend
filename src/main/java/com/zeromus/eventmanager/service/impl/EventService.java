package com.zeromus.eventmanager.service.impl;

import com.zeromus.eventmanager.exceptions.EventNotPublishedException;
import com.zeromus.eventmanager.model.dto.EventCardDto;
import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.dto.EventFormDto;
import com.zeromus.eventmanager.model.dto.UserDto;
import com.zeromus.eventmanager.model.entity.Event;
import com.zeromus.eventmanager.model.entity.EventCategory;
import com.zeromus.eventmanager.model.entity.EventWaitingList;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.zeromus.eventmanager.model.enums.EventState.PUBLISHED;
import static java.util.Collections.singletonList;

@Service("eventService")
@Transactional
public class EventService implements IEventService {

    private final EventRepository repository;
    private final EventMapper mapper;
    private final IUserService userService;
    private final UserMapper userMapper;
    private final EventCategoryService eventCategoryService;

    public EventService(final EventRepository repository, final EventMapper mapper, final IUserService userService, final UserMapper userMapper, final EventCategoryService eventCategoryService) {
        this.repository = repository;
        this.mapper = mapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.eventCategoryService = eventCategoryService;
    }

    public EventDto getEventById(Long id) {
        Optional<Event> event = repository.findById(id);
        EventDto dto = event.map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));
        UserDto currentUser = userMapper.toDto(getCurrentAuthenticatedUser());
        dto.setIsCurrentUserRegistered(dto.getParticipants().contains(currentUser));
        dto.setIsCurrentUserInWaitingList(dto.getWaitingList().contains(currentUser));
        return dto;
    }

    public List<EventDto> getAllEventsSearched(SearchEvent search) {
        return repository.findAll(search).stream().map(mapper::toDto).toList();
    }

    public Page<EventDto> getAllEventsPaged(SearchEvent search, Pageable pageable) {
        return repository.findAll(search, pageable).map(mapper::toDto);
    }

    public List<EventCardDto> getAllEventsList(int type, OffsetDateTime date) {
        OffsetDateTime startDate = date.truncatedTo(ChronoUnit.DAYS);
        OffsetDateTime endDate;

        switch (type) {
            case 2 -> {
                startDate = startDate.withDayOfMonth(1);
                endDate = startDate.plusMonths(1);
            }
            case 3 -> {
                startDate = startDate.withDayOfMonth(1).withMonth(1);
                endDate = startDate.plusYears(1);
            }
            default ->  endDate = startDate.plusDays(1);
        }
        return repository.findByStateInAndStartDateBetweenOrderByStartDate(singletonList(PUBLISHED), startDate, endDate)
                .stream().map(mapper::toCardDto).toList();
    }

    public EventDto addEvent(EventFormDto newDto) {
        Event newEvent = mapper.toEntity(newDto);
        newEvent.setLastUpdater(getCurrentAuthenticatedUser());
        newEvent.setState(EventState.DRAFT);
        newEvent.setCreatedDate(OffsetDateTime.now(ZoneId.systemDefault()));
        return mapper.toDto(repository.save(newEvent));
    }

    public EventDto updateEvent(EventFormDto updateDto) {
        Optional<Event> eventToUpdate = repository.findById(updateDto.getId());

        return eventToUpdate.map(event -> {
            event.setLastUpdater(getCurrentAuthenticatedUser());
            event.setLastUpdatedDate(OffsetDateTime.now(ZoneId.systemDefault()));
            event.setName(updateDto.getName());
            event.setDescription(updateDto.getDescription());
            event.setLocation(updateDto.getLocation());
            Set<EventCategory> categories = updateDto.getCategory().stream().map(c -> eventCategoryService.getEventCategoryEntityById(c.getId())).collect(Collectors.toSet());
            event.setCategory(categories);
            event.setState(updateDto.getState());
            event.setAllDay(updateDto.isAllDay());
            event.setStartDate(updateDto.getStartDate());
            event.setEndDate(updateDto.getEndDate());
            event.setSpotsAvailable(updateDto.getSpotsAvailable());
            return mapper.toDto(repository.save(event));
        }).orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + updateDto.getId()));
    }

    @Transactional
    public void addParticipant(Long idEvent, Long idUser) throws EventNotPublishedException {
        Optional<Event> optEvent = repository.findById(idEvent);
        if (optEvent.isPresent()) {
            Event event = optEvent.get();
            if(!event.getState().equals(PUBLISHED)){
                throw new EventNotPublishedException();
            }

            User user = userService.getUserEntityById(idUser);
            if(!Objects.isNull(event.getSpotsAvailable()) && event.getSpotsAvailable() > 0 && event.getParticipants().size() >= event.getSpotsAvailable()){
                event.addInWaintingList(user);
            } else {
                event.addParticipant(user);
            }
            repository.save(event);
        } else {
            throw new EntityNotFoundException("Event not found with ID: " + idEvent);
        }
    }

    public void removeParticipant(Long idEvent, Long idUser) {
        Optional<Event> optEvent = repository.findById(idEvent);
        if (optEvent.isPresent()) {
            Event event = optEvent.get();
            User user = userService.getUserEntityById(idUser);
            if(event.getParticipants().contains(user)) {
                event.removeParticipant(user);
                if (event.getSpotsAvailable() > 0 && !event.getWaitingList().isEmpty()) {
                    User next = repository.findNextUserInWaitingList(event.getId());
                    event.removeFromWaitingList(next);
                    event.addParticipant(next);
                }
            } else if (event.getWaitingList().stream().map(EventWaitingList::getUser).toList().contains(user)){
                event.removeFromWaitingList(user);
            }
            repository.save(event);
        } else {
            throw new EntityNotFoundException("Event not found with ID: " + idEvent);
        }
    }

    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            assert authentication != null;
            return userService.getUserEntityByUsername(authentication.getName());
        } else {
            throw new AuthenticationServiceException("User not authenticated");
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
