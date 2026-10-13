package com.zeromus.eventmanager.repository;

import com.zeromus.eventmanager.model.entity.EventCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventCategoryRepository extends CrudRepository<EventCategory, Long>, PagingAndSortingRepository<EventCategory, Long>, JpaSpecificationExecutor<EventCategory> {

    Optional<EventCategory> findByName(String name);
}
