package com.github.saleco.interview.calendar.api.agenda.service;

import com.github.saleco.interview.calendar.api.agenda.dto.AgendaDto;
import com.github.saleco.interview.calendar.api.agenda.dto.AvailabilityDto;
import com.github.saleco.interview.calendar.api.agenda.dto.CreateAgendaDto;
import com.github.saleco.interview.calendar.api.agenda.dto.SearchInterviewsAvailabilityDto;
import com.github.saleco.interview.calendar.api.agenda.mapper.AgendaMapper;
import com.github.saleco.interview.calendar.api.agenda.model.Agenda;
import com.github.saleco.interview.calendar.api.agenda.repository.AgendaRepository;
import com.github.saleco.interview.calendar.api.enums.UserType;
import com.github.saleco.interview.calendar.api.exception.NotFoundException;
import com.github.saleco.interview.calendar.api.exception.ValidationException;
import com.github.saleco.interview.calendar.api.mapper.DateMapper;
import com.github.saleco.interview.calendar.api.service.AbstractService;
import com.github.saleco.interview.calendar.api.user.dto.UserDto;
import com.github.saleco.interview.calendar.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AgendaServiceImpl extends AbstractService implements AgendaService {

    //Could come from application properties
    public static final int MAX_PERIOD_SEARCH_AVAILABILITY = 5;

    private final AgendaRepository agendaRepository;
    private final AgendaMapper agendaMapper;
    private final DateMapper dateMapper;
    private final UserRepository userRepository;

    @Override
    public AgendaDto createAgenda(AgendaDto agendaDto) {
        log.debug("Creating agenda: {}", agendaDto);

        validatesUserInput(agendaDto.getUserId());
        validateAgenda(agendaDto);

        return agendaMapper
          .modelToDto(agendaRepository.save(agendaMapper.dtoToModel(agendaDto)));
    }

    @Override
    public List<AgendaDto> createAgendas(List<AgendaDto> agendaDtos) {
        log.debug("Creating agendas: {}", agendaDtos);

        agendaDtos.forEach(agendaDto -> {
            validatesUserInput(agendaDto.getUserId());
            validateAgenda(agendaDto);
        });

        return agendaRepository
          .saveAll(agendaDtos.stream().map(agendaMapper::dtoToModel).collect(Collectors.toList()))
          .stream().map(agendaMapper::modelToDto).collect(Collectors.toList());
    }


    private void validateAgenda(AgendaDto agendaDto) {
        Example<Agenda> example = Example.of(agendaMapper.dtoToModel(agendaDto));
        if(agendaRepository.exists(example)){
            throw new ValidationException(String.format("Agenda already exists for %s", agendaDto));
        }

        validateAvailability(agendaDto.getStart(), agendaDto.getEnd());
    }

    @Override
    public Page<AgendaDto> getAvailability(SearchInterviewsAvailabilityDto searchInterviewsAvailabilityDto) {
        log.debug("Searching availability with: {}", searchInterviewsAvailabilityDto);

        validatesUserInputWithUserType(searchInterviewsAvailabilityDto.getCandidateId(), UserType.CANDIDATE);
        validatesUsersInputWithUserType(searchInterviewsAvailabilityDto.getInterviewerIds(), UserType.INTERVIEWER);
        validatesPeriodInput(searchInterviewsAvailabilityDto.getStartingFrom(), searchInterviewsAvailabilityDto.getEndingAt());

        long start = System.currentTimeMillis();

        Page<AgendaDto> agendaDtos = agendaRepository.searchAvailabilityBy(
          super.getPageRequest(searchInterviewsAvailabilityDto.getPageNumber(), searchInterviewsAvailabilityDto.getPageSize(), "start"),
          searchInterviewsAvailabilityDto.getCandidateId(),
          searchInterviewsAvailabilityDto.getInterviewerIds(),
          dateMapper.asTimestamp(searchInterviewsAvailabilityDto.getStartingFrom()),
          dateMapper.asTimestamp(searchInterviewsAvailabilityDto.getEndingAt()))
          .map(agendaMapper::modelToDto);

        long end = System.currentTimeMillis();
        log.debug("Availability search took :" + (end - start) + "ms");

        return agendaDtos;
    }

    @Override
    public List<AgendaDto> createAvailability(CreateAgendaDto createAgendaDto) {
        log.debug("Creating agenda slots: {}", createAgendaDto);

        validatesUserInput(createAgendaDto.getUserId());

        validateAvailabilities(createAgendaDto.getAvailabilities());

        //transforms availability periods into agendas
        List<AgendaDto> agendaDtos = getAgendaDtosFromAvailabilities(createAgendaDto.getAvailabilities(), createAgendaDto.getUserId());
        
        //to keep it simple, it will not validate each one of the slots if some of them
        //are duplicated in the list
        //it will check that on the create agenda method
        return this.createAgendas(agendaDtos);
    }

    private List<AgendaDto> getAgendaDtosFromAvailabilities(List<AvailabilityDto> availabilities, Long userId) {
        List<AgendaDto> agendaDtos = new ArrayList<>();

        availabilities.forEach(availability -> {
            for (LocalDateTime start = availability.getStart(); start.isBefore(availability.getEnd()) ; start = start.plusHours(1)) {
                agendaDtos.add(
                  AgendaDto.builder()
                    .start(OffsetDateTime.of(start, ZoneOffset.UTC))
                    .end(OffsetDateTime.of(start.plusHours(1), ZoneOffset.UTC))
                    .userId(userId)
                    .build());
            }
        });

        return agendaDtos;
    }

    @Override
    public void validateAvailabilities(List<AvailabilityDto> agendaSlots) {
        agendaSlots.forEach(agendaSlot ->
          this.validateAvailability(OffsetDateTime.of(agendaSlot.getStart(), ZoneOffset.UTC), OffsetDateTime.of(agendaSlot.getEnd(), ZoneOffset.UTC)));
    }

    @Override
    public void validateAvailability(OffsetDateTime start, OffsetDateTime end) {
        if(start == null || end == null) throw new IllegalArgumentException("Start / End should not be null");

        if(start.getMinute() != 0 || end.getMinute() != 0) {
            throw new ValidationException("Agenda Availability Start / End minute cannot be different then 00");
        }

        if(start.isAfter(end)) {
            throw new ValidationException("Agenda Availability End cannot be later then Agenda Availability Start");
        }
    }

    @Override
    public void validatesUserInput(Long userId) {
        if(userId == null) throw new IllegalArgumentException("userId should not be null");

        userRepository.findById(userId)
          .orElseThrow(() -> new NotFoundException(String.format("User %s not found", userId)));
    }

    public void validatesPeriodInput(OffsetDateTime startingFrom, OffsetDateTime endingAt) {
        if(startingFrom == null || endingAt == null) throw new IllegalArgumentException("startingFrom / endingAt should not be null");

        long daysBetween = startingFrom.until(endingAt, ChronoUnit.DAYS);

        if(daysBetween > MAX_PERIOD_SEARCH_AVAILABILITY) {
            throw new ValidationException("Search period shouldn't be greater than 5 days");
        }
    }

    @Override
    public void validatesUsersInputWithUserType(List<Long> userIds, UserType userType) {
        if(userIds == null || userType == null) throw new IllegalArgumentException("userIds / userType should not be null");
        userIds.forEach(userId -> validatesUserInputWithUserType(userId, userType));
    }

    @Override
    public void validatesUserInputWithUserType(Long userId, UserType userType) {
        if(userId == null || userType == null) throw new IllegalArgumentException("userId / userType should not be null");

        userRepository.findByIdAndUserType(userId, userType)
          .orElseThrow(() -> new NotFoundException(String.format("User %s, type %s not found", userId, userType)));
    }

}
