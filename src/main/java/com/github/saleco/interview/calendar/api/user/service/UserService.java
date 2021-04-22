package com.github.saleco.interview.calendar.api.user.service;

import com.github.saleco.interview.calendar.api.enums.UserType;
import com.github.saleco.interview.calendar.api.user.dto.UserDto;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDto save(UserDto userDto);
    Page<UserDto> getUsersByType(int page, int size, UserType userType);
}
