package com.github.saleco.interview.calendar.api.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saleco.interview.calendar.api.enums.UserType;
import com.github.saleco.interview.calendar.api.user.dto.CreateUserDto;
import com.github.saleco.interview.calendar.api.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UsersController.class)
class UsersControllerTest {

    public static final String USERS_API = "/ap1/v1/users";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @DisplayName("Given no Params when getUsers then Should use default values and  return status 200.")
    @Test
    void givenNoParamsWhenGetUsersThenShouldUseDefaultValuesAndReturnStatus200() throws Exception {
        mockMvc
          .perform(get(USERS_API))
          .andExpect(status().is2xxSuccessful());

        then(userService).should(times(1)).getUsersByType(0, 20, UserType.CANDIDATE);
        then(userService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Page and Size when getUses then Should return status 200.")
    @Test
    void givenPageSizeAndUserTypeWhenGetUsersThenShouldReturnStatus200() throws Exception {
        mockMvc
          .perform(get(USERS_API)
            .param("page", "0")
            .param("size", "20")
            .param("userType", UserType.INTERVIEWER.toString()))
          .andExpect(status().is2xxSuccessful());

        then(userService).should(times(1)).getUsersByType(0, 20, UserType.INTERVIEWER);
        then(userService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Invalid Request when createUser then Should return status 400.")
    @Test
    void givenInvalidRequestWhenCreateUserThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(post(USERS_API)
            .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("We are sorry, something unexpected happened. Please try it again later.")));

        then(userService).shouldHaveNoInteractions();
    }

    @DisplayName("Given request without mandatory fields when createUser then Should return status 400.")
    @Test
    void givenRequestWithoutMandatoryFieldsWhenCreateUserThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(post(USERS_API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(CreateUserDto.builder().build()))
          )
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("There is a validation rule that prevents the request.")));

        then(userService).shouldHaveNoInteractions();
    }

    @DisplayName("Given correct request when createUser then Should return status 200.")
    @Test
    void givenRequestWhenCreateUserThenShouldReturnStatus200() throws Exception {

        CreateUserDto createUserDto =
          CreateUserDto
            .builder()
            .name("User")
            .userType(UserType.CANDIDATE)
            .build();

        mockMvc
          .perform(post(USERS_API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createUserDto))
          )
          .andExpect(status().is2xxSuccessful());

        then(userService).should(times(1)).createUser(any(CreateUserDto.class));
        then(userService).shouldHaveNoMoreInteractions();
    }

}