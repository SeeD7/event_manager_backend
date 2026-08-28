package com.zeromus.eventmanager.repository;

import com.zeromus.eventmanager.model.entity.Event;
import com.zeromus.eventmanager.model.entity.User;
import com.zeromus.eventmanager.model.enums.EventState;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long>, PagingAndSortingRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @Query(value = "SELECT * FROM em_waiting_list wl INNER JOIN em_users eu on wl.user_id = eu.id WHERE event_id =?1 ORDER BY joined_at", nativeQuery = true)
    User findNextUserInWaitingList(Long eventId);

    List<Event> findByStateInAndStartDateBetweenOrderByStartDate(List<EventState> state, OffsetDateTime startDate, OffsetDateTime endDate);
}
