package com.github.saleco.interview.calendar.api.agenda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saleco.interview.calendar.api.agenda.dto.AvailabilityDto;
import com.github.saleco.interview.calendar.api.agenda.dto.CreateAgendaDto;
import com.github.saleco.interview.calendar.api.agenda.dto.SearchInterviewsAvailabilityDto;
import com.github.saleco.interview.calendar.api.agenda.service.AgendaService;
import com.github.saleco.interview.calendar.api.exception.NotFoundException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AgendasController.class)
class AgendasControllerTest {

    public static final String AGENDAS_API = "/ap1/v1/agendas";
    public static final String AGENDAS_SEARCH_API = "/ap1/v1/agendas/search";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AgendaService agendaService;


    @DisplayName("Given Invalid Request when getAgendas then Should return status 400.")
    @Test
    void givenInvalidRequestWhenGetAgendasThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(get(AGENDAS_SEARCH_API)
            .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("There is a validation rule that prevents the request.")));

        then(agendaService).shouldHaveNoInteractions();
    }

    @DisplayName("Given request without mandatory fields when getAgendas then Should return status 400.")
    @Test
    void givenRequestWithoutMandatoryFieldsWhenGetAgendasThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(get(AGENDAS_SEARCH_API)
            .contentType(MediaType.APPLICATION_JSON)
            .param("startingFrom", OffsetDateTime.now().toString())
            .param("endingAt",  OffsetDateTime.now().plusDays(5).toString()))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("There is a validation rule that prevents the request.")));

        then(agendaService).shouldHaveNoInteractions();
    }

    @DisplayName("Given correct request when getAgendas then Should return status 2XX.")
    @Test
    void givenCorrectRequestWhenGetAgendasThenStatusShouldBe2XX() throws Exception {

        mockMvc
          .perform(get(AGENDAS_SEARCH_API)
            .contentType(MediaType.APPLICATION_JSON)
            .param("startingFrom", OffsetDateTime.now().toString())
            .param("endingAt",  OffsetDateTime.now().plusDays(5).toString())
            .param("candidateId", "1")
            .param("interviewerIds", "1")
            .param("interviewerIds", "2")
          ).andExpect(status().is2xxSuccessful());

        then(agendaService).should(times(1)).getAvailability(any(SearchInterviewsAvailabilityDto.class));
        then(agendaService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Invalid Request when setupAgendaSlots then Should return status 400.")
    @Test
    void givenInvalidRequestWhenSetupAgendaSlotsThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(post(AGENDAS_API)
            .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("We are sorry, something unexpected happened. Please try it again later.")));

        then(agendaService).shouldHaveNoInteractions();
    }

    @DisplayName("Given request without mandatory fields when setupAgendaSlots then Should return status 400.")
    @Test
    void givenRequestWithoutMandatoryFieldsWhenSetupAgendaSlotsThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(post(AGENDAS_API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(CreateAgendaDto.builder().build()))
          )
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("There is a validation rule that prevents the request.")));

        then(agendaService).shouldHaveNoInteractions();
    }

    @DisplayName("Given request when setupAgendaSlots throws NotFoundException then Should return status 404.")
    @Test
    void givenRequesWhenSetupAgendaSlotsThrowsNotFoundExceptionThenShouldReturnStatus404() throws Exception {
        CreateAgendaDto createAgendaDto =
          CreateAgendaDto
            .builder()
            .userId(1L)
            .availabilities(Lists.newArrayList(AvailabilityDto.builder().start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(6)).build()))
            .build();

        doThrow(NotFoundException.class).when(agendaService).createAvailability(createAgendaDto);

        mockMvc
          .perform(post(AGENDAS_API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createAgendaDto))
          )
          .andExpect(status().isNotFound());

        then(agendaService).should(times(1)).createAvailability(any(CreateAgendaDto.class));
        then(agendaService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given correct request when setupAgendaSlots then Should return status 200.")
    @Test
    void givenRequestWhenSetupAgendaSlotsThenShouldReturnStatus200() throws Exception {

        CreateAgendaDto createAgendaDto =
          CreateAgendaDto
            .builder()
            .userId(1L)
            .availabilities(Lists.newArrayList(AvailabilityDto.builder().start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(6)).build()))
            .build();

        mockMvc
          .perform(post(AGENDAS_API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createAgendaDto))
          )
          .andExpect(status().is2xxSuccessful());

        then(agendaService).should(times(1)).createAvailability(any(CreateAgendaDto.class));
        then(agendaService).shouldHaveNoMoreInteractions();
    }

}