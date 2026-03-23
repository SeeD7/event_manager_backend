package com.zeromus.eventmanager.repository;

import com.zeromus.eventmanager.model.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    Page<User> findAll(@NonNull Pageable pageable);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
