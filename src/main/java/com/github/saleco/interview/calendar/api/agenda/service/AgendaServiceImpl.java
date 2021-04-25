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


    /**
     * This method creates an Agenda for an User
     *
     * @param  agendaDto  an Agenda to be created
     * @return the Agenda created
     */
    @Override
    public AgendaDto createAgenda(AgendaDto agendaDto) {
        log.debug("Creating agenda: {}", agendaDto);

        validatesUserInput(agendaDto.getUserId());
        validateAgenda(agendaDto);

        return agendaMapper
          .modelToDto(agendaRepository.save(agendaMapper.dtoToModel(agendaDto)));
    }

    /**
     * This method creates multiple Agendas for an User
     *
     * @param  agendaDtos  The list of Agendas to be created
     * @return the List of Agendas created
     */
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

    /**
     * Given a Search Interviews Availability this method will search
     * all the available possible times between a Candidate and one or more Interviewers
     * A start and end must be provided with the maximum period of 5 days
     *
     * @param  searchInterviewsAvailabilityDto  Search criterias
     * @return the List of availabilities for the given criteria
     * @see SearchInterviewsAvailabilityDto
     */
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

    /**
     * Given a List of availabilities and an User, creates multiple agenda slots
     *
     * @param  createAgendaDto  Create Agenda parameters
     * @return the List of created Agendas
     * @see CreateAgendaDto
     */
    @Override
    public List<AgendaDto> createAvailability(CreateAgendaDto createAgendaDto) {
        log.debug("Creating agenda slots: {}", createAgendaDto);

        validatesUserInput(createAgendaDto.getUserId());

        validateAvailabilities(createAgendaDto.getAvailabilities());

        //transforms availability periods into agendas
        List<AgendaDto> agendaDtos = getAgendaDtosFromAvailabilities(createAgendaDto.getAvailabilities(), createAgendaDto.getUserId());
        
        //to keep it simple, it will not validate each one of the slots if some of them
        //are duplicated in the received list
        //it will check that on the create agenda method
        return this.createAgendas(agendaDtos);
    }


    /**
     * Given an Agenda, checks if the Agenda already exists
     * for and User, start and end
     * @throws ValidationException when agenda already exists
     * @param  agendaDto  Create Agenda parameters
     * @see AgendaDto
     */
    @Override
    public void validateAgenda(AgendaDto agendaDto) {
        Example<Agenda> example = Example.of(agendaMapper.dtoToModel(agendaDto));
        if(agendaRepository.exists(example)){
            throw new ValidationException(String.format("Agenda already exists for %s", agendaDto));
        }

        validateAvailability(agendaDto.getStart(), agendaDto.getEnd());
    }

    /**
      * This method is responsible for transforming a list of availabilities
     * into 1 hour slots the each one of the given periods in each availability
     *
     * @param  availabilities List of availabilities
     * @param  userId The owner of availabilities
     * @see AvailabilityDto
     */
    public List<AgendaDto> getAgendaDtosFromAvailabilities(List<AvailabilityDto> availabilities, Long userId) {
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

    /**
     * Wrapper method to Validate a listof the availabilities
     *
     * @param  availabilityDtos List of availabilities
     * @see AvailabilityDto
     */
    @Override
    public void validateAvailabilities(List<AvailabilityDto> availabilityDtos) {
        availabilityDtos.forEach(agendaSlot ->
          this.validateAvailability(OffsetDateTime.of(agendaSlot.getStart(), ZoneOffset.UTC), OffsetDateTime.of(agendaSlot.getEnd(), ZoneOffset.UTC)));
    }

    /**
     * Given a start and end validates the availability
     * @throws ValidationException In case of minute is not the initial minute of an hour
     * @throws ValidationException In case of start is after end
     * @param  start Start date time
     * @param  end   End date time
     */
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

    /**
     * Validates if the User exists
     * @throws IllegalArgumentException In case the user id is null
     * @throws NotFoundException In case the user doesn't exists
     * @param  userId User Identification
     */
    @Override
    public void validatesUserInput(Long userId) {
        if(userId == null) throw new IllegalArgumentException("userId should not be null");

        userRepository.findById(userId)
          .orElseThrow(() -> new NotFoundException(String.format("User %s not found", userId)));
    }

    /**
     * Given start and end it validates Period Input
     * This validation was created to avoid any performance issues when searching for an interview
     * @throws ValidationException In case the difference between start and end are greater then 5 days
     * @throws IllegalArgumentException In case startingFrom / endingAt are null
     * @param  startingFrom Starting From date time
     * @param  endingAt Ending At date time
     */
    public void validatesPeriodInput(OffsetDateTime startingFrom, OffsetDateTime endingAt) {
        if(startingFrom == null || endingAt == null) throw new IllegalArgumentException("startingFrom / endingAt should not be null");

        long daysBetween = startingFrom.until(endingAt, ChronoUnit.DAYS);

        if(daysBetween > MAX_PERIOD_SEARCH_AVAILABILITY) {
            throw new ValidationException("Search period shouldn't be greater than 5 days");
        }
    }

    /**
     * Wrapper method to validate a given list of User ids and a User Type, belongs to the the provided type
     * @throws IllegalArgumentException In case userIds / userType are null
     * @param  userIds The users to validate
     * @param  userType The Type of the user
     * @see UserType
     */
    @Override
    public void validatesUsersInputWithUserType(List<Long> userIds, UserType userType) {
        if(userIds == null || userType == null) throw new IllegalArgumentException("userIds / userType should not be null");
        userIds.forEach(userId -> validatesUserInputWithUserType(userId, userType));
    }

    /**
     * Method to validate an User belongs to the the provided user type
     * @throws IllegalArgumentException In case userId / userType are null
     * @throws NotFoundException In case user doesn't belong to the provided userType
     * @param  userId The user to validate
     * @param  userType The Type of the user
     * @see UserType
     */
    @Override
    public void validatesUserInputWithUserType(Long userId, UserType userType) {
        if(userId == null || userType == null) throw new IllegalArgumentException("userId / userType should not be null");

        userRepository.findByIdAndUserType(userId, userType)
          .orElseThrow(() -> new NotFoundException(String.format("User %s, type %s not found", userId, userType)));
    }

}
