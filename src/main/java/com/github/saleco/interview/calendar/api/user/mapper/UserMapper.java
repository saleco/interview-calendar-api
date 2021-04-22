package com.github.saleco.interview.calendar.api.user.mapper;

import com.github.saleco.interview.calendar.api.mapper.DateMapper;
import com.github.saleco.interview.calendar.api.user.dto.UserDto;
import com.github.saleco.interview.calendar.api.user.model.User;
import org.mapstruct.Mapper;

@Mapper(uses = DateMapper.class)
public interface UserMapper {
    UserDto modelToDto(User user);
    User dtoToModel(UserDto userDto);

}
