package com.github.saleco.interview.calendar.api.agenda.service;

import com.github.saleco.interview.calendar.api.agenda.dto.AgendaDto;
import com.github.saleco.interview.calendar.api.agenda.dto.AvailabilityDto;
import com.github.saleco.interview.calendar.api.agenda.dto.CreateAgendaDto;
import com.github.saleco.interview.calendar.api.agenda.dto.SearchInterviewsAvailabilityDto;
import com.github.saleco.interview.calendar.api.enums.UserType;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;
import java.util.List;

public interface AgendaService {

    AgendaDto createAgenda(AgendaDto agendaDto);
    List<AgendaDto> createAgendas(List<AgendaDto> agendaDtos);
    Page<AgendaDto> getAvailability(SearchInterviewsAvailabilityDto searchInterviewsAvailabilityDto);

    void validateAvailability(OffsetDateTime start, OffsetDateTime end);
    void validateAvailabilities(List<AvailabilityDto> agendaSlots);
    void validatesUserInput(Long userId);
    void validatesPeriodInput(OffsetDateTime startingFrom, OffsetDateTime endingAt);
    void validatesUsersInputWithUserType(List<Long> userIds, UserType userType);
    List<AgendaDto> createAvailability(CreateAgendaDto createAgendaDto);
    void validatesUserInputWithUserType(Long userId, UserType userType);
}
