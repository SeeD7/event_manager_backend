package com.zeromus.eventmanager.model.search;

import com.zeromus.eventmanager.model.entity.Event;
import com.zeromus.eventmanager.model.entity.EventCategory;
import com.zeromus.eventmanager.model.entity.EventCategory_;
import com.zeromus.eventmanager.model.entity.Event_;
import com.zeromus.eventmanager.model.enums.EventState;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchEvent implements Specification<Event> {
    private String name;
    private String description;
    private Set<Long> categories;
    private Set<EventState> states;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;

    @Override
    public Predicate toPredicate(@NonNull Root<Event> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        final List<Predicate> predicates = new ArrayList<>();

        if (isNotBlank(name)) {
            predicates.add(cb.like(cb.lower(root.get(Event_.NAME)), "%" + name.toLowerCase() + "%"));
        }

        if (isNotBlank(description)) {
            predicates.add(cb.like(cb.lower(root.get(Event_.DESCRIPTION)), "%" + description.toLowerCase() + "%"));
        }

        Predicate finalPredicate = predicates.isEmpty() ? cb.conjunction() : cb.or(predicates.toArray(Predicate[]::new));

        if (Objects.nonNull(startDate)) {
            finalPredicate = cb.and(cb.greaterThan(root.get(Event_.startDate), startDate), finalPredicate);
        }

        if (Objects.nonNull(endDate)) {
            finalPredicate = cb.and(cb.lessThan(root.get(Event_.END_DATE), endDate), finalPredicate);
        }

        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            Join<Event, EventCategory> categoryJoin = root.join(Event_.CATEGORY);

            // 2. On applique un simple .in() sur l'ID de la catégorie jointe !
            finalPredicate = cb.and(categoryJoin.get(EventCategory_.ID).in(categories), finalPredicate);
        }

        if (Objects.nonNull(states) && !states.isEmpty()) {
            final List<Predicate> statePredicates = new ArrayList<>();
            for (EventState sta : states) {
                statePredicates.add(cb.like(root.get(Event_.STATE), sta.toString()));
            }
            finalPredicate = cb.and(cb.or(statePredicates.toArray(Predicate[]::new)), finalPredicate);
        }

        return finalPredicate;
    }
}