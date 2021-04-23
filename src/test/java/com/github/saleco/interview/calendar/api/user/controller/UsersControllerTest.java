package com.github.saleco.interview.calendar.api.user.controller;

import com.github.saleco.interview.calendar.api.enums.UserType;
import com.github.saleco.interview.calendar.api.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UsersController.class)
class UsersControllerTest {

    public static final String PATIENTS_API = "/ap1/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @DisplayName("Given no Params when getUsers then Should use default values and  return status 200.")
    @Test
    void givenNoParamsWhenGetUsersThenShouldUseDefaultValuesAndReturnStatus200() throws Exception {
        mockMvc
          .perform(get(PATIENTS_API))
          .andExpect(status().is2xxSuccessful());

        then(userService).should(times(1)).getUsersByType(0, 20, UserType.CANDIDATE);
        then(userService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Page and Size when getUses then Should return status 200.")
    @Test
    void givenPageSizeAndUserTypeWhenGetUsersThenShouldReturnStatus200() throws Exception {
        mockMvc
          .perform(get(PATIENTS_API)
            .param("page", "0")
            .param("size", "20")
            .param("userType", UserType.INTERVIEWER.toString()))
          .andExpect(status().is2xxSuccessful());

        then(userService).should(times(1)).getUsersByType(0, 20, UserType.INTERVIEWER);
        then(userService).shouldHaveNoMoreInteractions();
    }

}