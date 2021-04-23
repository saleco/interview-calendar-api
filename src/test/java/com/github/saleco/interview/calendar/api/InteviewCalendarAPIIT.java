package com.github.saleco.interview.calendar.api;

import com.github.saleco.interview.calendar.api.agenda.dto.AgendaDto;
import com.github.saleco.interview.calendar.api.agenda.dto.AvailabilityDto;
import com.github.saleco.interview.calendar.api.agenda.dto.CreateAgendaDto;
import com.github.saleco.interview.calendar.api.agenda.dto.SearchInterviewsAvailabilityDto;
import com.github.saleco.interview.calendar.api.agenda.service.AgendaService;
import com.github.saleco.interview.calendar.api.enums.UserType;
import com.github.saleco.interview.calendar.api.user.dto.CreateUserDto;
import com.github.saleco.interview.calendar.api.user.dto.UserDto;
import com.github.saleco.interview.calendar.api.user.service.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

/*
    Integration tests to validate Interview Calendar API Use Cases
 */
@SpringBootTest
public class InteviewCalendarAPIIT {

    public static final int YEAR = 2021;
    public static final int MONTH = 4;
    public static final int MINUTE = 0;
    public static final int SECOND = 0;
    public static final int NANO_OF_SECOND = 0;

    @Autowired
    AgendaService agendaService;

    @Autowired
    UserService userService;

    private UserDto david;
    private UserDto ingrid;
    private UserDto carl;
    private UserDto ines;

    private List<AgendaDto> davidAgendas;
    private List<AgendaDto> ingridAgendas;
    private List<AgendaDto> carlAgendas;

    @BeforeEach
    public void setup() {
        david = createUser("David", UserType.INTERVIEWER);
        ingrid = createUser("Ingrid", UserType.INTERVIEWER);
        ines = createUser("Ines", UserType.INTERVIEWER);
        carl = createUser("Carl", UserType.CANDIDATE);
        davidAgendas = createAvailability(david, getDavidAvailabilities());
        ingridAgendas = createAvailability(ingrid, getIngridAvailabilities());
        carlAgendas = createAvailability(carl, getCarlAvailabilities());
    }

    @DisplayName("As an INTERVIEWER, I would like to set availability slots - David is available next week each day from 9am through 4pm without breaks")
    @Test
    void givenDavidAsInterviewerAndAvailabilitiesWhenSetupAvailabilityThenShouldCreateAvailabitiesForDavid() {

        //asserting agenda size (7 slots a day / 5 days - 35 slots)
        assertThat(davidAgendas.size()).isEqualTo(35);

        //0 to 6 - first day
        //7 to 13 - second day
        //...
        //28 to 34 - last day
        assertThat(davidAgendas.get(0).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 26, 9, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(davidAgendas.get(0).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 26, 10, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(davidAgendas.get(6).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 26, 15, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(davidAgendas.get(6).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 26, 16, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(davidAgendas.get(7).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 27, 9, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(davidAgendas.get(7).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 27, 10, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(davidAgendas.get(13).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 27, 15, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(davidAgendas.get(13).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 27, 16, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(davidAgendas.get(28).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 30, 9, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(davidAgendas.get(28).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 30, 10, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(davidAgendas.get(34).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 30, 15, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(davidAgendas.get(34).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 30, 16, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));

    }

    @DisplayName("As an INTERVIEWER, I would like to set availability slots - " +
      "Ingrid is available from 12pm to 6pm on Monday and Wednesday next week, and from 9am to 12pm on Tuesday and Thursday")
    @Test
    void givenIngridAsInterviewerAndAvailabilitiesWhenSetupAvailabilityThenShouldCreateAvailabitiesForIngrid() {
        //asserting agenda size (6 slots 2 days + 3 slots 2 days - 18 slots )
        assertThat(ingridAgendas.size()).isEqualTo(18);

        //0 to 5 - first day
        //6 to 8 - second day
        //...
        //15 to 17 - last day
        assertThat(ingridAgendas.get(0).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 26, 12, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(ingridAgendas.get(0).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 26, 13, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(ingridAgendas.get(5).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 26, 17, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(ingridAgendas.get(5).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 26, 18, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(ingridAgendas.get(6).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 27, 9, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(ingridAgendas.get(6).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 27, 10, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(ingridAgendas.get(8).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 27, 11, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(ingridAgendas.get(8).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 27, 12, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(ingridAgendas.get(15).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 29, 9, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(ingridAgendas.get(15).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 29, 10, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(ingridAgendas.get(17).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 29, 11, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(ingridAgendas.get(17).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 29, 12, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));

    }

    @DisplayName("As a CANDIDATE, I would like to set availability slots - " +
      "Carl is available for the interview from 9am to 10am any weekday next week and from 10am to 12pm on Wednesday")
    @Test
    void givenCarlAsCandidateAndAvailabilitiesWhenSetupAvailabilityThenShouldCreateAvailabitiesForCarl() {
        //asserting agenda size (1 slot 4 days + 2 slots 1 day - 6 slots )
        assertThat(carlAgendas.size()).isEqualTo(6);

        //0 - first day
        //1 - second day
        //2 to 3 - third day
        //4 - fourth day
        //5 - fourth day
        assertThat(carlAgendas.get(0).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 26, 9, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(carlAgendas.get(0).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 26, 10, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(carlAgendas.get(1).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 27, 9, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(carlAgendas.get(1).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 27, 10, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(carlAgendas.get(2).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 28, 10, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(carlAgendas.get(2).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 28, 11, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(carlAgendas.get(3).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 28, 11, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(carlAgendas.get(3).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 28, 12, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(carlAgendas.get(4).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 29, 9, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(carlAgendas.get(4).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 29, 10, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(carlAgendas.get(5).getStart()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 30, 9, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));
        assertThat(carlAgendas.get(5).getEnd()).isEqualTo(OffsetDateTime.of(YEAR, MONTH, 30, 10, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC));

    }

    @DisplayName("As a USER, I would like to get a list of possible interview slots for a particular candidate and one or more interviewers - " +
      "In this example, if the API queries for the candidate Carl and interviewers Ines and Ingrid, the response should be" +
      " a collection of 1-hour slots: from 9am to 10am on Tuesday, from 9am to 10am on Thursday.")
    @Test
    void givenCarlAsCandidateIngridAndDavidAsInterviewrsWhenSearchAvailabilityThenShouldReturnAvailabilities() {
        Page<AgendaDto> agendaDtos = agendaService.getAvailability(
          SearchInterviewsAvailabilityDto.builder()
            .candidateId(carl.getId())
            .interviewerIds(Lists.newArrayList(ingrid.getId(), ines.getId()))
            .startingFrom(OffsetDateTime.of(YEAR, MONTH, 26, 9, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC))
            .endingAt(OffsetDateTime.of(YEAR, MONTH, 30, 18, MINUTE, SECOND, NANO_OF_SECOND, ZoneOffset.UTC))
            .build()
        );

        assertThat(agendaDtos).isNotNull();
        assertThat(agendaDtos).isNotEmpty();

    }

    private List<AgendaDto> createAvailability(UserDto userDto, List<AvailabilityDto> availabilities) {
        return agendaService.createAvailability(
          CreateAgendaDto.builder()
            .userId(userDto.getId())
            .availabilities(availabilities)
            .build()
        );
    }

    private List<AvailabilityDto> getDavidAvailabilities() {
        //David is available next week each day from 9am through 4pm without breaks
        //considering only weekdays
        return Lists.newArrayList(
          buildAvailabilityDto(YEAR, MONTH, 26, 9, 16),
          buildAvailabilityDto(YEAR, MONTH, 27, 9, 16),
          buildAvailabilityDto(YEAR, MONTH, 28, 9, 16),
          buildAvailabilityDto(YEAR, MONTH, 29, 9, 16),
          buildAvailabilityDto(YEAR, MONTH, 30, 9, 16)
        );
    }

    private List<AvailabilityDto> getIngridAvailabilities() {
        //Ingrid is available from 12pm to 6pm on Monday and Wednesday next week, and from 9am to 12pm on Tuesday and Thursday
        return Lists.newArrayList(
          buildAvailabilityDto(YEAR, MONTH, 26, 12, 18),
          buildAvailabilityDto(YEAR, MONTH, 27, 9, 12),
          buildAvailabilityDto(YEAR, MONTH, 28, 12, 18),
          buildAvailabilityDto(YEAR, MONTH, 29, 9, 12)
        );
    }

    private List<AvailabilityDto> getCarlAvailabilities() {
        //Carl is available for the interview from 9am to 10am any weekday next week and from 10am to 12pm on Wednesday
        return Lists.newArrayList(
          buildAvailabilityDto(YEAR, MONTH, 26, 9, 10),
          buildAvailabilityDto(YEAR, MONTH, 27, 9, 10),
          buildAvailabilityDto(YEAR, MONTH, 28, 10, 12),
          buildAvailabilityDto(YEAR, MONTH, 29, 9, 10),
          buildAvailabilityDto(YEAR, MONTH, 30, 9, 10)
        );
    }

    protected AvailabilityDto buildAvailabilityDto(int year, int month, int dayOfMonth, int beginHour, int endHour) {
        return
          AvailabilityDto.builder()
          .start(LocalDateTime.of(year, month, dayOfMonth, beginHour, MINUTE, SECOND, NANO_OF_SECOND))
          .end(LocalDateTime.of(year, month, dayOfMonth, endHour, MINUTE, SECOND, NANO_OF_SECOND))
          .build();
    }

    protected UserDto createUser(String name, UserType userType) {
        return userService.createUser(
          CreateUserDto.builder().name(name).userType(userType).build()
        );
    }

}
