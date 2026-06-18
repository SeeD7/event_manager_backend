package com.zeromus.eventmanager.controller;

import com.zeromus.eventmanager.model.dto.EventCategoryDto;
import com.zeromus.eventmanager.service.EventCategoryService;
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
@RequestMapping("event-category")
public class EventCategoryController {

    private final EventCategoryService service;

    public EventCategoryController(final EventCategoryService service) {
        this.service = service;
    }

    /**
     * Create - Add a new EventCategory
     *
     * @param EventCategory An object EventCategory
     * @return The EventCategory object saved
     */
    @PostMapping
    public ResponseEntity<EventCategoryDto> createEventCategory(@Valid @RequestBody EventCategoryDto EventCategory) {
        try {
            return new ResponseEntity<>(service.addEventCategory(EventCategory), CREATED);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Read - Get one EventCategory
     *
     * @param id The id of the EventCategory
     * @return An EventCategory object full filled
     */
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<EventCategoryDto> getEventCategory(@PathVariable final Long id) {
        try {
            return new ResponseEntity<>(service.getEventCategoryById(id), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Read - Get all EventCategory
     *
     * @return - An Iterable object of EventCategory full filled
     */
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @GetMapping
    public ResponseEntity<List<EventCategoryDto>> getAllEventCategory() {
        try {
            return new ResponseEntity<>(service.getAll(), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Read - Get all EventCategory
     *
     * @return - An Iterable object of EventCategory full filled
     */
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @GetMapping("/paged")
    public ResponseEntity<Page<EventCategoryDto>> getEventCategoryPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAllEventCategoryPaged(pageable), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Update - Update an existing EventCategory
     *
     * @param id   - The id of the EventCategory to update
     * @param EventCategory - The EventCategory object updated
     * @return the updated EventCategory
     */
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<EventCategoryDto> updateEventCategory(@PathVariable final Long id, @RequestBody EventCategoryDto EventCategory) {
        try {
            return new ResponseEntity<>(service.updateEventCategory(id, EventCategory), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Delete - Delete an EventCategory
     *
     * @param id - The id of the EventCategory to delete
     */
    @RolesAllowed({"ADMIN"})
    @DeleteMapping("/{id}")
    public void deleteEventCategory(@PathVariable final Long id) {
        service.deleteEventCategory(id);
    }
}
