package com.zeromus.eventmanager.controller;

import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.search.SearchEvent;
import com.zeromus.eventmanager.service.EventService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin
@RequestMapping("user")
public class EventController {

    private final EventService service;

    public EventController(final EventService service) {
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
     * @param id   - The id of the event to update
     * @param event - The event object updated
     * @return the updated event
     */
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @PutMapping("/event/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable final Long id, @RequestBody EventDto event) {
        try {
            return new ResponseEntity<>(service.updateEvent(id, event), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Delete - Delete an event
     *
     * @param id - The id of the event to delete
     */
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @DeleteMapping("/event/{id}")
    public void deleteEvent(@PathVariable final Long id) {
        service.deleteEvent(id);
    }
}
