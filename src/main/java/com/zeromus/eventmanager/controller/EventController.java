package com.zeromus.eventmanager.controller;

import com.zeromus.eventmanager.exceptions.EventNotPublishedException;
import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.enums.EventState;
import com.zeromus.eventmanager.model.search.SearchEvent;
import com.zeromus.eventmanager.service.IEventService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@CrossOrigin
@RequestMapping("user")
public class EventController {

    private final IEventService service;

    public EventController(final IEventService service) {
        this.service = service;
    }

    /**
     * Create - Add a new event
     *
     * @param event An object event
     * @return The event object saved
     */
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @PostMapping("/event")
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventDto event) {
        try {
            return new ResponseEntity<>(service.addEvent(event), CREATED);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Read - Get one event
     *
     * @param id The id of the event
     * @return An Event object full filled
     */
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @GetMapping("/event/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable final Long id) {
        try {
            return new ResponseEntity<>(service.getEventById(id), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Read - Get all events
     *
     * @return - An Iterable object of Event full filled
     */
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @GetMapping("/events/search")
    public ResponseEntity<List<EventDto>> getEventsSearched(SearchEvent search) {
        try {
            return new ResponseEntity<>(service.getAllEventsSearched(search), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Read - Get all events
     *
     * @return - An Iterable object of Event full filled
     */
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @GetMapping("/events/page")
    public ResponseEntity<Page<EventDto>> getEventsPaged(SearchEvent search, Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAllEventsPaged(search, pageable), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Update - Update an existing event
     *
     * @param event - The event object updated
     * @return the updated event
     */
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @PutMapping("/event")
    public ResponseEntity<EventDto> updateEvent(@Valid @RequestBody EventDto event) {
        try {
            return new ResponseEntity<>(service.updateEvent(event), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Update - Add a user on an Event if there's spot available
     *
     * @param idEvent The id of the event the user would like to participate to
     * @param idUser The id of the user
     * @return If there's enough spot available
     */
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @PutMapping("/event/{idEvent}/{idUser}")
    public ResponseEntity<Boolean> participate(@PathVariable final Long idEvent, @PathVariable final Long idUser) {
        try {
            return new ResponseEntity<>(service.addParticipant(idEvent, idUser), CREATED);
        } catch (EventNotPublishedException _) {
            return new ResponseEntity<>(NOT_ACCEPTABLE);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Delete - remove a user from an Event
     *
     * @param idEvent The id of the event the user would like to not participate to
     * @param idUser The id of the user
     * @return The event object saved
     */
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @DeleteMapping("/event/{idEvent}/{idUser}")
    public ResponseEntity<Void> cancel(@PathVariable final Long idEvent, @PathVariable final Long idUser) {
        try {
            service.removeParticipant(idEvent, idUser);
            return new ResponseEntity<>(OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Update - Put an event at published state
     *
     * @param idEvent The id of the event to publish
     * @return The event object saved
     */
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @PutMapping("/event/{idEvent}/publish")
    public ResponseEntity<EventDto> publish(@PathVariable final Long idEvent) {
        try {
            return new ResponseEntity<>(service.changeState(idEvent, EventState.PUBLISHED), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Update - Put an event at draft state
     *
     * @param idEvent The id of the event the user would like put in draft state
     * @return The event object saved
     */
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @PutMapping("/event/{idEvent}/draft")
    public ResponseEntity<EventDto> draft(@PathVariable final Long idEvent) {
        try {
            return new ResponseEntity<>(service.changeState(idEvent, EventState.DRAFT), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Update - Put an event at deleted state
     *
     * @param idEvent The id of the event the user would like to cancel
     * @return The event object saved
     */
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @PutMapping("/event/{idEvent}/delete")
    public ResponseEntity<Void> delete(@PathVariable final Long idEvent) {
        try {
            service.changeState(idEvent, EventState.DELETED);
            return new ResponseEntity<>(OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }
}
