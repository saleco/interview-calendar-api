package com.github.saleco.interview.calendar.api.user.repository;

import com.github.saleco.interview.calendar.api.enums.UserType;
import com.github.saleco.interview.calendar.api.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndUserType(Long id, UserType userType);
    Page<User> findALlByUserType(UserType userType, Pageable pageable);
    Optional<User> findFirstByUserTypeAndName(UserType userType, String name);
}
