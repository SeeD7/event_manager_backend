package com.zeromus.eventmanager.repository;

import com.zeromus.eventmanager.model.entity.Event;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends CrudRepository<Event, Long>, PagingAndSortingRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Optional<Event> findByName(String name);
}
