package com.zeromus.eventmanager.model.dto;

import com.zeromus.eventmanager.model.entity.User;
import com.zeromus.eventmanager.model.entity.User_;
import com.zeromus.eventmanager.model.enums.UserRole;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserDto implements Specification<User> {
    private String firstName;
    private String lastName;
    private String username;
    private List<UserRole> role;
    private String email;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        final List<Predicate> predicates = new ArrayList<>();

        if (isNotBlank(firstName)) {
            predicates.add(cb.like(cb.lower(root.get(User_.FIRST_NAME)), firstName.toLowerCase() + "%"));
        }

        if (isNotBlank(lastName)) {
            predicates.add(cb.like(cb.lower(root.get(User_.LAST_NAME)), lastName.toLowerCase() + "%"));
        }

        if (isNotBlank(username)) {
            predicates.add(cb.like(cb.lower(root.get(User_.USERNAME)), username.toLowerCase() + "%"));
        }

        if (isNotBlank(email)) {
            predicates.add(cb.like(cb.lower(root.get(User_.EMAIL)), email.toLowerCase() + "%"));
        }

        Predicate finalPredicate = predicates.isEmpty() ? cb.conjunction() : cb.or(predicates.toArray(Predicate[]::new));

        if (Objects.nonNull(role) && !role.isEmpty()) {
            return cb.and(root.get(User_.ROLE).in(role), finalPredicate);
        }

        return finalPredicate;
    }
}