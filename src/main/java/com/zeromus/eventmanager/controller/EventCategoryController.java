package com.zeromus.eventmanager.controller;

import com.zeromus.eventmanager.model.dto.EventCategoryDto;
import com.zeromus.eventmanager.model.dto.EventCategoryLightDto;
import com.zeromus.eventmanager.service.IEventCategoryService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
//@CrossOrigin
@RequestMapping("event-category")
public class EventCategoryController {

    private final IEventCategoryService service;

    public EventCategoryController(final IEventCategoryService service) {
        this.service = service;
    }

    /**
     * Create - Add a new EventCategory
     *
     * @param eventCategory An object EventCategory
     * @return The EventCategory object saved
     */
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @PostMapping
    public ResponseEntity<EventCategoryDto> createEventCategory(@Valid @RequestBody EventCategoryDto eventCategory) {
        try {
            return new ResponseEntity<>(service.addEventCategory(eventCategory), CREATED);
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
     * Read - Get one EventCategory by his name
     *
     * @param name The name of the EventCategory
     * @return An EventCategory object full filled
     */
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @GetMapping("/name")
    public ResponseEntity<EventCategoryDto> getEventCategoryByName(@RequestParam final String name) {
        return service.getEventCategoryByName(name)
                .map(categoryDto -> new ResponseEntity<>(categoryDto, OK))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    /**
     * Read - Verify if a category name already exists
     *
     * @param name The name of the category
     * @return True if it exists, else false
     */
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(@RequestParam final String name) {
        return service.getEventCategoryByName(name)
                .map(_ -> new ResponseEntity<>(true, OK))
                .orElseGet(() -> new ResponseEntity<>(false, OK));
    }

    /**
     * Read - Get all EventCategory
     *
     * @return - An Iterable object of EventCategory full filled
     */
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @GetMapping
    public ResponseEntity<List<EventCategoryLightDto>> getAllEventCategory() {
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
     * @param id            - The id of the EventCategory to update
     * @param eventCategory - The EventCategory object updated
     * @return the updated EventCategory
     */
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<EventCategoryDto> updateEventCategory(@PathVariable final Long id, @RequestBody EventCategoryDto eventCategory) {
        try {
            return new ResponseEntity<>(service.updateEventCategory(id, eventCategory), OK);
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
