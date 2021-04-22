package com.github.saleco.interview.calendar.api.agenda.controller;

import com.github.saleco.interview.calendar.api.agenda.dto.AgendaDto;
import com.github.saleco.interview.calendar.api.agenda.dto.CreateAgendaDto;
import com.github.saleco.interview.calendar.api.agenda.dto.SearchInterviewsAvailabilityDto;
import com.github.saleco.interview.calendar.api.agenda.service.AgendaService;
import com.github.saleco.interview.calendar.api.utils.InterviewCalendarAPIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ap1/v1/agendas")
@Tag(name = "Agendas's API", description = "Resources related to Agendas's information")
@RequiredArgsConstructor
@Slf4j
public class AgendasController {

    private final AgendaService agendaService;

    @Operation(summary = "Search Interview's Availability",
        description = "As a USER, I would like to get a list of possible interview slots for a particular candidate and one or more interviewers.")
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The list of Interview's Availability has been returned",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = AgendaDto.class)))),
      @ApiResponse(responseCode = "400", description = "Invalid parameter",
      content = @Content(schema = @Schema(implementation = InterviewCalendarAPIResponse.class)))})
    public Page<AgendaDto> getAgendas(
      @ParameterObject @Valid SearchInterviewsAvailabilityDto searchInterviewsAvailabilityDto) {
        return agendaService.getAvailability(searchInterviewsAvailabilityDto);
    }

    @Operation(summary = "Setup Agenda slots",
      description = "As an INTERVIEWER / CANDIDATE, I would like to set availability slots")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The Agenda slots were successfully created.",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = AgendaDto.class)))),
      @ApiResponse(responseCode = "400", description = "Invalid parameter",
        content = @Content(schema = @Schema(implementation = InterviewCalendarAPIResponse.class))),
      @ApiResponse(responseCode = "404", description = "Not found",
        content = @Content(schema = @Schema(implementation = InterviewCalendarAPIResponse.class)))})
    public List<AgendaDto> setupAgendaSlots(
      @Parameter(name = "Create Agenda DTO", required = true) @RequestBody @Valid CreateAgendaDto createAgendaDto) {
        return agendaService.createAvailability(createAgendaDto);
    }



}


