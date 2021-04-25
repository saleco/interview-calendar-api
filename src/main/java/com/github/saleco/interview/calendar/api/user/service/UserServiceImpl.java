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

    /**
     * Get Users by UserType paginated
     *
     * @param  page - 0 Indexed Page (default value 0)
     * @param  size - Size of the Page (default value 20)
     * @param  userType - The type of the User
     * @return the Page of Users for the given criteria
     * @see UserType
     * @see Page
     */
    @Override
    public Page<UserDto> getUsersByType(int page, int size, UserType userType) {
        log.debug("Searching users by page {}, pageSize {} and type {}.", page, size, userType);

        return
          userRepository
            .findALlByUserType(userType, PageRequest.of(page, size))
            .map(userMapper::modelToDto);
    }

    /**
     * Creates an User - The User can be of Type Candidate / Interviewer
     *
     * @param  createUserDto - User values
     * @return the created User
     * @see CreateUserDto
     */
    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        log.debug("Creating user {}", createUserDto);

        UserDto userDto = getUserDtoFromCreateUserDto(createUserDto);

        return userMapper
          .modelToDto(userRepository.save(userMapper.dtoToModel(userDto)));
    }

    /**
     * Transforms a CreateUserDto into UserDto
     *
     * @param  createUserDto - User values
     * @return UserDto
     * @see CreateUserDto
     */
    @Override
    public UserDto getUserDtoFromCreateUserDto(CreateUserDto createUserDto) {
        return UserDto
          .builder()
          .userType(createUserDto.getUserType())
          .name(createUserDto.getName())
          .build();
    }

}
