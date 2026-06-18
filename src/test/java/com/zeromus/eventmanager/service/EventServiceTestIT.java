package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.search.SearchEvent;
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
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Logger;

import static com.zeromus.eventmanager.model.enums.EventState.DELETED;
import static com.zeromus.eventmanager.model.enums.EventState.DRAFT;
import static java.util.Collections.singleton;

@SpringBootTest
@ActiveProfiles("test")
class EventServiceTestIT {

    private static final Logger LOGGER = Logger.getLogger(EventServiceTestIT.class.getName());
    private static final Long CAT_SPORT = 1L;
    private static final Long CAT_PERSO = 2L;

    @Autowired
    private EventService service;

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
        Assertions.assertThat(result.getTotalElements()).isEqualTo(5);
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
        SearchEvent search = SearchEvent.builder().states(singleton(DRAFT)).build();
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));
        Page<EventDto> result = service.getAllEventsPaged(search, pageable);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    void getAllEvents_WhenSearchedByMultipleState_ShouldReturnEventsPaged() {
        SearchEvent search = SearchEvent.builder().states(new HashSet<>(Arrays.asList(DRAFT, DELETED))).build();
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

    @Test
    void getAllEvents_WhenSearchedByNameAndDescription_ShouldReturnEventsPaged() {
        SearchEvent search = SearchEvent.builder().description("immersion").name("Event").build();
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<EventDto> result = service.getAllEventsPaged(search, pageable);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(2);
    }
}
