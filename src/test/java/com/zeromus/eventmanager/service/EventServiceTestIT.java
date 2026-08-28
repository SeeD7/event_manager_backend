package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.exceptions.EventNotPublishedException;
import com.zeromus.eventmanager.model.dto.EventCardDto;
import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.dto.EventFormDto;
import com.zeromus.eventmanager.model.enums.EventState;
import com.zeromus.eventmanager.model.search.SearchEvent;
import com.zeromus.eventmanager.utils.AssertionUtils;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionSystemException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static com.zeromus.eventmanager.model.enums.EventState.DELETED;
import static com.zeromus.eventmanager.model.enums.EventState.DRAFT;
import static com.zeromus.eventmanager.utils.EventUtils.*;
import static java.time.OffsetDateTime.parse;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EventServiceTestIT {

    private static final Logger LOGGER = Logger.getLogger(EventServiceTestIT.class.getName());
    private static final Long CAT_SPORT = 1L;
    private static final Long CAT_PERSO = 2L;

    @Autowired
    private IEventService service;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            // Lance le script UNE SEULE FOIS pour toute la classe de test
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("db/event/base.sql"));
        } catch (Exception e) {
            LOGGER.warning(Arrays.toString(e.getStackTrace()));
        }
    }

    @Test
    void getAllEvents_WhenSearchedEmpty_ShouldReturnEventsPaged() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));
        Page<EventDto> result = service.getAllEventsPaged(new SearchEvent(), pageable);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(14);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    void getAllEvents_WhenSearchedByCategory_ShouldReturnEventsPaged() {
        SearchEvent search = SearchEvent.builder().categories(singleton(CAT_SPORT)).build();
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));
        Page<EventDto> result = service.getAllEventsPaged(search, pageable);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    void getAllEvents_WhenSearchedByMultipleCategory_ShouldReturnEventsPaged() {
        SearchEvent search = SearchEvent.builder().categories(new HashSet<>(Arrays.asList(CAT_SPORT, CAT_PERSO))).build();
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<EventDto> result = service.getAllEventsPaged(search, pageable);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(4);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    void getAllEvents_WhenSearchedByState_ShouldReturnEventsPaged() {
        SearchEvent search = SearchEvent.builder().state(singleton(DRAFT)).build();
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));
        Page<EventDto> result = service.getAllEventsPaged(search, pageable);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    void getAllEvents_WhenSearchedByMultipleState_ShouldReturnEventsPaged() {
        SearchEvent search = SearchEvent.builder().state(new HashSet<>(Arrays.asList(DRAFT, DELETED))).build();
        Pageable pageable = PageRequest.of(1, 1, Sort.by(Sort.Direction.ASC, "id"));
        Page<EventDto> result = service.getAllEventsPaged(search, pageable);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    void getAllEvents_WhenSearchedByRoleAndName_ShouldReturnEventsPaged() {
        SearchEvent search = SearchEvent.builder().categories(singleton(CAT_PERSO)).name("Event").build();
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<EventDto> result = service.getAllEventsPaged(search, pageable);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(1);
    }

    @WithMockUser(username = "Lulu", roles = {"ADMIN"})
    @Test
    void getListEvents_WhenSearchedByDay_ShouldReturnEventsListed() {
        List<EventCardDto> result = service.getAllEventsList(1, OffsetDateTime.parse("2026-04-20T10:30:00-05:00"));
        Assertions.assertThat(result).hasSize(1);
    }

    @WithMockUser(username = "Lulu", roles = {"ADMIN"})
    @Test
    void getListEvents_WhenSearchedByMonth_ShouldReturnEventsListed() {
        List<EventCardDto> result = service.getAllEventsList(2, OffsetDateTime.parse("2026-07-20T10:30:00-05:00"));
        Assertions.assertThat(result).hasSize(1);
    }

    @WithMockUser(username = "Lulu", roles = {"ADMIN"})
    @Test
    void getListEvents_WhenSearchedByYear_ShouldReturnEventsListed() {
        List<EventCardDto> result = service.getAllEventsList(3, OffsetDateTime.parse("2025-06-20T10:30:00-05:00"));
        Assertions.assertThat(result).hasSize(1);
    }

    @Test
    void getAllEvents_WhenSearchedByNameAndDescription_ShouldReturnEventsPaged() {
        SearchEvent search = SearchEvent.builder().description("immersion").name("Event").build();
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<EventDto> result = service.getAllEventsPaged(search, pageable);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(2);
    }

    @WithMockUser(username = "Lulu", roles = {"ADMIN"})
    @Test
    void createEvent_WhenEndDateBeforeStartDate_ShouldReturnException() {
        EventFormDto newDto = EventFormDto.builder().id(null).name("Test").description("Test").location("")
                .category(Set.of(CATEGORY_PERSO_LIGHT)).state(EventState.PUBLISHED).allDay(false)
                .startDate(parse("2026-06-17T18:30:00+02:00"))
                .endDate(parse("2026-06-16T20:00:00+02:00")).spotsAvailable(1L)
                .build();

        Exception ex = assertThrows(ConstraintViolationException.class, () -> service.addEvent(newDto));

        AssertionUtils.assertExceptionMessageContains(ex, "La date de début doit être antérieure à la date de fin.");

    }

    @WithMockUser(username = "Lulu", roles = {"ADMIN"})
    @Test
    void updateEvent_WhenEndDateBeforeStartDate_ShouldReturnException() {
        EventFormDto updateDto = createValidTestEventFormDto();
        updateDto.setEndDate(parse("2026-06-16T20:00:00+02:00"));

        TransactionSystemException ex = assertThrows(TransactionSystemException.class, () -> service.updateEvent(updateDto)
        );

        // 2. On descend à la racine pour trouver la vraie ConstraintViolationException
        Throwable rootCause = ex.getRootCause();

        Assertions.assertThat(rootCause).isInstanceOf(ConstraintViolationException.class);
        assert rootCause != null;
        Assertions.assertThat(rootCause.getMessage()).contains("La date de début doit être antérieure à la date de fin.");
    }

    @WithMockUser(username = "Lulu", roles = {"ADMIN"})
    @Test
    void participateEvent_WhenNoSpotAvailableDefined_ShouldHaveParticipant() throws EventNotPublishedException {
        service.addParticipant(1L,1L);
        EventDto result = service.getEventById(1L);
        assertEquals(1, result.getParticipants().size());
        assertEquals(0, result.getWaitingList().size());
    }

    @WithMockUser(username = "Lulu", roles = {"ADMIN"})
    @Test
    void participateEvent_WhenSpotAvailable_ShouldHaveParticipant() throws EventNotPublishedException {
        service.addParticipant(2L,1L);
        EventDto result = service.getEventById(2L);
        assertEquals(1, result.getParticipants().size());
        assertEquals(0, result.getWaitingList().size());
    }

    @WithMockUser(username = "Lulu", roles = {"ADMIN"})
    @Test
    void participateEvent_WhenNoSpotAvailable_ShouldHaveInWaitinglist() throws EventNotPublishedException {
        service.addParticipant(3L,1L);
        service.addParticipant(3L,2L);
        EventDto result = service.getEventById(3L);
        assertEquals(1, result.getParticipants().size());
        assertEquals(1, result.getWaitingList().size());
    }

    @WithMockUser(username = "Lulu", roles = {"ADMIN"})
    @Test
    void removeParticipantEvent_WhenSpotAvailableDefined_ShouldMoveToParticipant() throws EventNotPublishedException {
        service.addParticipant(6L,1L);
        service.addParticipant(6L,2L);
        service.removeParticipant(6L,1L);
        EventDto result = service.getEventById(6L);
        assertEquals(1, result.getParticipants().size());
        assertEquals(0, result.getWaitingList().size());
    }
}
