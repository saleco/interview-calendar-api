package com.github.saleco.interview.calendar.api.user.service;

import com.github.saleco.interview.calendar.api.enums.UserType;
import com.github.saleco.interview.calendar.api.service.AbstractService;
import com.github.saleco.interview.calendar.api.user.dto.CreateUserDto;
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
    public Page<UserDto> getUsersByType(int page, int size, UserType userType) {
        log.debug("Searching users by page {}, pageSize {} and type {}.", page, size, userType);

        return
          userRepository
            .findALlByUserType(userType, PageRequest.of(page, size))
            .map(userMapper::modelToDto);
    }

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        log.debug("Creating user {}", createUserDto);

        UserDto userDto = getUserDtoFromCreateUserDto(createUserDto);

        return userMapper
          .modelToDto(userRepository.save(userMapper.dtoToModel(userDto)));
    }

    @Override
    public UserDto getUserDtoFromCreateUserDto(CreateUserDto createUserDto) {
        return UserDto
          .builder()
          .userType(createUserDto.getUserType())
          .name(createUserDto.getName())
          .build();
    }

}
