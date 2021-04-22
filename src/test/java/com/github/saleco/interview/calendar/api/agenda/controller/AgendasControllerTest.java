package com.github.saleco.interview.calendar.api.agenda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saleco.interview.calendar.api.agenda.dto.SearchInterviewsAvailabilityDto;
import com.github.saleco.interview.calendar.api.agenda.service.AgendaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
            .content(objectMapper.writeValueAsString(SearchInterviewsAvailabilityDto.builder().build()))
          )
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("There is a validation rule that prevents the request.")));

        then(agendaService).shouldHaveNoInteractions();
    }

//    @DisplayName("Given correct request when getAgendas then Should return status 2XX.")
//    @Test
//    void givenCorrectRequestWhenGetAgendasThenStatusShouldBe2XX() throws Exception {
//        SearchInterviewsAvailabilityDto searchInterviewsAvailabilityDto = SearchInterviewsAvailabilityDto
//          .builder()
//          .startingFrom(OffsetDateTime.now())
//          .endingAt(OffsetDateTime.now().plusHours(5))
//          .build();
//
//        mockMvc
//          .perform(get(AGENDAS_SEARCH_API)
//            .contentType(MediaType.APPLICATION_JSON)
//            .param("startingFrom", searchInterviewsAvailabilityDto.getStartingFrom().toString())
//            .param("endingAt", searchInterviewsAvailabilityDto.getEndingAt().toString())
//          ).andExpect(status().is2xxSuccessful());
//
//        then(agendaService).should(times(1)).getAvailability(any(SearchInterviewsAvailabilityDto.class));
//        then(agendaService).shouldHaveNoMoreInteractions();
//    }


}