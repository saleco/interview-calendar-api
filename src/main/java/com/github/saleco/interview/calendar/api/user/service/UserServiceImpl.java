package com.github.saleco.interview.calendar.api.user.service;

import com.github.saleco.interview.calendar.api.enums.UserType;
import com.github.saleco.interview.calendar.api.service.AbstractService;
import com.github.saleco.interview.calendar.api.user.dto.UserDto;
import com.github.saleco.interview.calendar.api.user.mapper.UserMapper;
import com.github.saleco.interview.calendar.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends AbstractService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto save(UserDto userDto) {
        log.debug("Creating user {}", userDto);
        return userMapper
          .modelToDto(userRepository.save(userMapper.dtoToModel(userDto)));
    }

    @Override
    public Page<UserDto> getUsersByType(int page, int size, UserType userType) {
        log.debug("Searching users by page {}, pageSize {} and type {}.", page, size, userType);

        return
          userRepository
            .findALlByUserType(userType, PageRequest.of(page, size))
            .map(userMapper::modelToDto);
    }

}
