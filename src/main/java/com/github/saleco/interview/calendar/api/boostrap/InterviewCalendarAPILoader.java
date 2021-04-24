package com.github.saleco.interview.calendar.api.boostrap;

import com.github.javafaker.Faker;
import com.github.saleco.interview.calendar.api.agenda.dto.AgendaDto;
import com.github.saleco.interview.calendar.api.agenda.dto.SearchInterviewsAvailabilityDto;
import com.github.saleco.interview.calendar.api.agenda.service.AgendaService;
import com.github.saleco.interview.calendar.api.enums.UserType;
import com.github.saleco.interview.calendar.api.user.dto.CreateUserDto;
import com.github.saleco.interview.calendar.api.user.dto.UserDto;
import com.github.saleco.interview.calendar.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Load inital data for Interview Calendar API
 */
@Component
@RequiredArgsConstructor
public class InterviewCalendarAPILoader implements CommandLineRunner {

    private static final Faker faker = new Faker(LocaleContextHolder.getLocale());
    public static final int INITAL_WORKING_HOUR = 9;
    public static final int FINAL_WORKING_HOUR = 19;
    public static final int TEN_DAYS = 10;

    private final UserService userService;
    private final AgendaService agendaService;

    @Override
    public void run(String... args) throws Exception {

        //creating interviewers
        for (int i = 0; i < 10; i++) {
            UserDto userDto = userService.createUser(createUser(UserType.INTERVIEWER));

            //creates an agenda for the next 10 days
            for(OffsetDateTime today = OffsetDateTime.now(); today.isBefore(OffsetDateTime.now().plusDays(TEN_DAYS)); today = today.plusDays(1)) {
                 for (int hour = INITAL_WORKING_HOUR; hour < FINAL_WORKING_HOUR; hour++) {
                    agendaService.createAgenda(createAgenda(userDto.getId(), today, hour));
                }
            }
        }

        //creating candidates
        for (int i = 0; i < 10; i++) {
            UserDto userDto = userService.createUser(createUser(UserType.CANDIDATE));

            //creates an agenda for the next 10 days
            for(OffsetDateTime today = OffsetDateTime.now(); today.isBefore(OffsetDateTime.now().plusDays(TEN_DAYS)); today = today.plusDays(1)) {
                for (int hour = INITAL_WORKING_HOUR; hour < FINAL_WORKING_HOUR; hour++) {
                    agendaService.createAgenda(createAgenda(userDto.getId(), today, hour));
                }
            }
        }

        //gets a random candidate
        UserDto randomCandidate = userService.getUsersByType(0, 1, UserType.CANDIDATE).getContent().get(0);

        //gets 2 random interviewers
        List<UserDto> randomInterviewers = userService.getUsersByType(0, 2,  UserType.INTERVIEWER).getContent();

        //finds availability between candidate and interviewers
        List<AgendaDto> agendaDto = getAgendaAvaliability(randomCandidate.getId(), randomInterviewers.stream().map(UserDto::getId).collect(Collectors.toList()));
        System.out.println(agendaDto);
    }

    private List<AgendaDto> getAgendaAvaliability(Long candidateId, List<Long> interviewerIds) {
        Page<AgendaDto> agendaDtoPage = agendaService.getAvailability(
            SearchInterviewsAvailabilityDto
              .builder()
              .candidateId(candidateId)
              .interviewerIds(interviewerIds)
              .startingFrom(OffsetDateTime.now())
              .endingAt(OffsetDateTime.now().plusDays(5))
              .build()
          );
        return agendaDtoPage.getContent();
    }

    private AgendaDto createAgenda(Long userId, OffsetDateTime today, int hour) {
        return AgendaDto
          .builder()
          .start(today.withHour(hour).withMinute(0).withSecond(0))
          .end(today.withHour(hour+1).withMinute(0).withSecond(0))
          .userId(userId)
          .build();
    }

    private CreateUserDto createUser(UserType userType) {
        return CreateUserDto
          .builder()
          .name(faker.name().fullName())
          .userType(userType)
          .build();
    }
}
