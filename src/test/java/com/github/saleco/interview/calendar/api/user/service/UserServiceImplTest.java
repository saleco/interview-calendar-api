package com.github.saleco.interview.calendar.api.user.service;

import com.github.saleco.interview.calendar.api.enums.UserType;
import com.github.saleco.interview.calendar.api.user.dto.CreateUserDto;
import com.github.saleco.interview.calendar.api.user.dto.UserDto;
import com.github.saleco.interview.calendar.api.user.mapper.UserMapper;
import com.github.saleco.interview.calendar.api.user.model.User;
import com.github.saleco.interview.calendar.api.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    UserService userServiceSpy;

    @BeforeEach
    void setup() {
        this.userServiceSpy = spy(userService);
    }

    @DisplayName("Given Page, Size and UserType When getUsersByType then should return Page of UserDto")
    @Test
    void getUsersByType() {
        User user = mock(User.class);
        UserDto userDto = mock(UserDto.class);

        given(userRepository.findALlByUserType(UserType.CANDIDATE, PageRequest.of(0, 20)))
          .willReturn(new PageImpl<>(Collections.singletonList(user)));

        given(userMapper.modelToDto(user)).willReturn(userDto);

        Page<UserDto> userDtos = userService.getUsersByType(0, 20, UserType.CANDIDATE);

        Assertions.assertAll(
          () -> assertThat(userDtos).isNotNull(),
          () -> assertThat(userDtos).hasSize(1)
        );

        then(userRepository).should(times(1)).findALlByUserType(any(UserType.class), any(PageRequest.class));
        then(userMapper).should(times(1)).modelToDto(any(User.class));
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userMapper).shouldHaveNoMoreInteractions();

    }

    @DisplayName("Given Create User Dto When Save then should return UserDto")
    @Test
    void givenCreateUserDtoWhenSaveThenShouldReturnUserDto() {
        CreateUserDto createUserDto = mock(CreateUserDto.class);

        UserDto userDto = UserDto.builder().build();

        User user = User.builder().build();

        doReturn(userDto).when(userServiceSpy).getUserDtoFromCreateUserDto(createUserDto);

        given(userMapper.modelToDto(user)).willReturn(userDto);
        given(userMapper.dtoToModel(userDto)).willReturn(user);
        given(userRepository.save(user)).willReturn(user);


        UserDto userDtoReturned = userServiceSpy.createUser(createUserDto);

        assertThat(userDtoReturned).isNotNull();

        then(userRepository).should(times(1)).save(user);
        then(userMapper).should(times(1)).modelToDto(user);
        then(userMapper).should(times(1)).dtoToModel(userDto);
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userMapper).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Create User Dto When GetUserDtoFromCreateUserDto then should return UserDto")
    @Test
    void givenCreateUserDtoWhenGetUserDtoFromCreateUserDtoThenShouldReturnUserDto() {
        CreateUserDto createUserDto = mock(CreateUserDto.class);
        assertThat(userService.getUserDtoFromCreateUserDto(createUserDto)).isNotNull();
    }
}