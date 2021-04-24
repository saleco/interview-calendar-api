package com.github.saleco.interview.calendar.api.agenda.service;

import com.github.saleco.interview.calendar.api.agenda.dto.AgendaDto;
import com.github.saleco.interview.calendar.api.agenda.dto.SearchInterviewsAvailabilityDto;
import com.github.saleco.interview.calendar.api.agenda.mapper.AgendaMapper;
import com.github.saleco.interview.calendar.api.agenda.model.Agenda;
import com.github.saleco.interview.calendar.api.agenda.repository.AgendaRepository;
import com.github.saleco.interview.calendar.api.enums.UserType;
import com.github.saleco.interview.calendar.api.exception.NotFoundException;
import com.github.saleco.interview.calendar.api.exception.ValidationException;
import com.github.saleco.interview.calendar.api.mapper.DateMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceImplTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private AgendaMapper agendaMapper;

    @Mock
    private DateMapper dateMapper;

    @InjectMocks
    private AgendaServiceImpl agendaService;

    private AgendaService agendaServiceSpy;

    @BeforeEach
    void setUp() {
        agendaServiceSpy = spy(agendaService);
    }

    @DisplayName("Given Agenda DTO When createAgenda then validateUserInput throws IllegalArgumentsException")
    @Test
    void givenAgendaDTOWhenCreateAgendaThenShouldThrowIllegalArgumentsException() {
        doThrow(IllegalArgumentException.class).when(agendaServiceSpy).validatesUserInput(1L);
        Assertions.assertThrows(IllegalArgumentException.class,
          () -> agendaServiceSpy.createAgenda(AgendaDto.builder().userId(1L).build()));

        then(agendaRepository).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInput(anyLong());
        then(agendaServiceSpy).should(times(1)).createAgenda(any(AgendaDto.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Agenda DTO When createAgenda then validateUserInput throws NotFoundException")
    @Test
    void givenAgendaDTOWhenCreateAgendaThenShouldThrowNotFoundException() {
        doThrow(NotFoundException.class).when(agendaServiceSpy).validatesUserInput(1L);
        Assertions.assertThrows(NotFoundException.class,
          () -> agendaServiceSpy.createAgenda(AgendaDto.builder().userId(1L).build()));

        then(agendaRepository).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInput(anyLong());
        then(agendaServiceSpy).should(times(1)).createAgenda(any(AgendaDto.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Agenda DTO When createAgenda then validateAgenda throws ValidationException")
    @Test
    void givenAgendaDTOWhenCreateAgendaThenShouldValidationException() {
        AgendaDto agendaDto = AgendaDto.builder().userId(1L).build();
        doNothing().when(agendaServiceSpy).validatesUserInput(1L);
        doThrow(ValidationException.class).when(agendaServiceSpy).validateAgenda(agendaDto);

        Assertions.assertThrows(ValidationException.class,
          () -> agendaServiceSpy.createAgenda(agendaDto));

        then(agendaRepository).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInput(anyLong());
        then(agendaServiceSpy).should(times(1)).validateAgenda(any(AgendaDto.class));
        then(agendaServiceSpy).should(times(1)).createAgenda(any(AgendaDto.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Agenda DTO When createAgenda then should return AgendaDto")
    @Test
    void givenAgendaDTOWhenCreateAgendaThenShouldReturnAgendaDto() {
        AgendaDto agendaDto = AgendaDto.builder().userId(1L).build();
        Agenda agenda = Agenda.builder().build();

        given(agendaMapper.modelToDto(agenda)).willReturn(agendaDto);
        given(agendaMapper.dtoToModel(agendaDto)).willReturn(agenda);
        given(agendaRepository.save(agenda)).willReturn(agenda);

        doNothing().when(agendaServiceSpy).validatesUserInput(1L);
        doNothing().when(agendaServiceSpy).validateAgenda(agendaDto);

        AgendaDto agendaDtoReturned = agendaServiceSpy.createAgenda(agendaDto);

        assertThat(agendaDtoReturned).isNotNull();
        assertThat(agendaDtoReturned.getUserId()).isEqualTo(1L);

        then(agendaRepository).should(times(1)).save(any(Agenda.class));
        then(agendaMapper).should(times(1)).dtoToModel(any(AgendaDto.class));
        then(agendaMapper).should(times(1)).modelToDto(any(Agenda.class));
        then(agendaServiceSpy).should(times(1)).validatesUserInput(anyLong());
        then(agendaServiceSpy).should(times(1)).validateAgenda(any(AgendaDto.class));
        then(agendaServiceSpy).should(times(1)).createAgenda(any(AgendaDto.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();

    }

    @DisplayName("Given List of Agenda DTO When createAgendas then validateUserInput throws IllegalArgumentsException")
    @Test
    void givenAgendaDtoListWhenCreateAgendasThenShouldThrowIllegalArgumentsException() {
        doThrow(IllegalArgumentException.class).when(agendaServiceSpy).validatesUserInput(anyLong());
        Assertions.assertThrows(IllegalArgumentException.class,
          () -> agendaServiceSpy.createAgendas(Collections.singletonList(AgendaDto.builder().userId(1L).build())));

        then(agendaRepository).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInput(anyLong());
        then(agendaServiceSpy).should(times(1)).createAgendas(any(List.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given List of Agenda DTO When createAgendas then validateUserInput throws NotFoundException")
    @Test
    void givenAgendaDtoListWhenCreateAgendasThenShouldThrowNotFoundException() {
        doThrow(NotFoundException.class).when(agendaServiceSpy).validatesUserInput(1L);
        Assertions.assertThrows(NotFoundException.class,
          () -> agendaServiceSpy.createAgendas(Collections.singletonList(AgendaDto.builder().userId(1L).build())));

        then(agendaRepository).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInput(anyLong());
        then(agendaServiceSpy).should(times(1)).createAgendas(any(List.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given List of Agenda DTO When createAgendas then validateAgenda throws ValidationException")
    @Test
    void givenAgendaDtoListWhenCreateAgendasThenShouldValidationException() {
        AgendaDto agendaDto = AgendaDto.builder().userId(1L).build();
        doNothing().when(agendaServiceSpy).validatesUserInput(1L);
        doThrow(ValidationException.class).when(agendaServiceSpy).validateAgenda(any(AgendaDto.class));

        Assertions.assertThrows(ValidationException.class,
          () -> agendaServiceSpy.createAgendas(Collections.singletonList(agendaDto)));

        then(agendaRepository).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInput(anyLong());
        then(agendaServiceSpy).should(times(1)).validateAgenda(any(AgendaDto.class));
        then(agendaServiceSpy).should(times(1)).createAgendas(any(List.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given List of Agenda DTO When createAgendas then should return List of AgendaDto")
    @Test
    void givenAgendaDtoListWhenCreateAgendasThenShouldReturnAgendaDtoList() {
        AgendaDto agendaDto = AgendaDto.builder().userId(1L).build();
        Agenda agenda = Agenda.builder().build();

        given(agendaMapper.modelToDto(agenda)).willReturn(agendaDto);
        given(agendaMapper.dtoToModel(agendaDto)).willReturn(agenda);
        given(agendaRepository.saveAll(Collections.singletonList(agenda))).willReturn(Collections.singletonList(agenda));

        doNothing().when(agendaServiceSpy).validatesUserInput(anyLong());
        doNothing().when(agendaServiceSpy).validateAgenda(any(AgendaDto.class));

        List<AgendaDto> agendaDtosReturned = agendaServiceSpy.createAgendas(Collections.singletonList(agendaDto));

        assertThat(agendaDtosReturned).isNotNull();
        assertThat(agendaDtosReturned).isNotEmpty();
        assertThat(agendaDtosReturned.get(0).getUserId()).isEqualTo(1L);

        then(agendaRepository).should(times(1)).saveAll(any(List.class));
        then(agendaMapper).should(times(1)).dtoToModel(any(AgendaDto.class));
        then(agendaMapper).should(times(1)).modelToDto(any(Agenda.class));
        then(agendaServiceSpy).should(times(1)).validatesUserInput(anyLong());
        then(agendaServiceSpy).should(times(1)).validateAgenda(any(AgendaDto.class));
        then(agendaServiceSpy).should(times(1)).createAgendas(any(List.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();

    }

    @DisplayName("Given SearchInterviewAvailability DTO When getAvailability then validatesUserInputWithUserType throws IllegalArgumentException")
    @Test
    void givenSearchInterviewAvailabilityDTOWhenGetAvailabilityValidatesUserInputWithUserTypeThenShouldThrowIllegalArgumentException() {
        doThrow(IllegalArgumentException.class).when(agendaServiceSpy).validatesUserInputWithUserType(anyLong(), any(UserType.class));
        Assertions.assertThrows(IllegalArgumentException.class,
          () -> agendaServiceSpy.getAvailability(SearchInterviewsAvailabilityDto.builder().candidateId(1L).build()));

        then(agendaRepository).shouldHaveNoInteractions();
        then(dateMapper).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInputWithUserType(anyLong(), any(UserType.class));
        then(agendaServiceSpy).should(times(1)).getAvailability(any(SearchInterviewsAvailabilityDto.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given SearchInterviewAvailability DTO When getAvailability then validatesUserInputWithUserType throws NotFoundException")
    @Test
    void givenSearchInterviewAvailabilityDTOWhenGetAvailabilityValidatesUserInputWithUserTypeThenShouldThrowNotFoundException() {
        doThrow(NotFoundException.class).when(agendaServiceSpy).validatesUserInputWithUserType(anyLong(), any(UserType.class));
        Assertions.assertThrows(NotFoundException.class,
          () -> agendaServiceSpy.getAvailability(SearchInterviewsAvailabilityDto.builder().candidateId(1L).build()));

        then(agendaRepository).shouldHaveNoInteractions();
        then(dateMapper).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInputWithUserType(anyLong(), any(UserType.class));
        then(agendaServiceSpy).should(times(1)).getAvailability(any(SearchInterviewsAvailabilityDto.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given SearchInterviewAvailability DTO When getAvailability then validatesUsersInputWithUserType throws IllegalArgumentsException")
    @Test
    void givenSearchInterviewAvailabilityDTOWhengetAvailabilityValidatesUsersInputWithUserTypeThenShouldThrowIllegalArgumentsException() {
        doNothing().when(agendaServiceSpy).validatesUserInputWithUserType(anyLong(), any(UserType.class));
        doThrow(IllegalArgumentException.class).when(agendaServiceSpy).validatesUsersInputWithUserType(anyList(), any(UserType.class));

        Assertions.assertThrows(IllegalArgumentException.class,
          () -> agendaServiceSpy.getAvailability(SearchInterviewsAvailabilityDto.builder().candidateId(1L).interviewerIds(Collections.emptyList()).build()));

        then(agendaRepository).shouldHaveNoInteractions();
        then(dateMapper).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInputWithUserType(anyLong(), any(UserType.class));
        then(agendaServiceSpy).should(times(1)).validatesUsersInputWithUserType(anyList(), any(UserType.class));
        then(agendaServiceSpy).should(times(1)).getAvailability(any(SearchInterviewsAvailabilityDto.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given SearchInterviewAvailability DTO When getAvailability then validatesPeriodInput throws IllegalArgumentsException")
    @Test
    void givenSearchInterviewAvailabilityDTOWhenGetAvailabilityValidatesPeriodInputThenShouldThrowIllegalArgumentsException() {
        doNothing().when(agendaServiceSpy).validatesUserInputWithUserType(anyLong(), any(UserType.class));
        doNothing().when(agendaServiceSpy).validatesUsersInputWithUserType(anyList(), any(UserType.class));
        doThrow(IllegalArgumentException.class).when(agendaServiceSpy).validatesPeriodInput(any(OffsetDateTime.class), any(OffsetDateTime.class));

        Assertions.assertThrows(IllegalArgumentException.class,
          () -> agendaServiceSpy.getAvailability(
            SearchInterviewsAvailabilityDto
              .builder()
              .candidateId(1L)
              .interviewerIds(Collections.emptyList())
              .startingFrom(OffsetDateTime.now())
              .endingAt(OffsetDateTime.now().plusDays(5))
              .build()));

        then(agendaRepository).shouldHaveNoInteractions();
        then(dateMapper).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInputWithUserType(anyLong(), any(UserType.class));
        then(agendaServiceSpy).should(times(1)).validatesUsersInputWithUserType(anyList(), any(UserType.class));
        then(agendaServiceSpy).should(times(1)).validatesPeriodInput(any(OffsetDateTime.class), any(OffsetDateTime.class));
        then(agendaServiceSpy).should(times(1)).getAvailability(any(SearchInterviewsAvailabilityDto.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given SearchInterviewAvailability DTO When getAvailability then should return Page of AgendaDto")
    @Test
    void givenSearchInterviewAvailabilityDTOWhenGetAvailabilityThenShouldReturnAgendaDtoPage() {
        AgendaDto agendaDto = AgendaDto.builder().userId(1L).build();
        Agenda agenda = Agenda.builder().build();

        doNothing().when(agendaServiceSpy).validatesUserInputWithUserType(anyLong(), any(UserType.class));
        doNothing().when(agendaServiceSpy).validatesUsersInputWithUserType(anyList(), any(UserType.class));
        doNothing().when(agendaServiceSpy).validatesPeriodInput(any(OffsetDateTime.class), any(OffsetDateTime.class));

        given(dateMapper.asTimestamp(any(OffsetDateTime.class))).willReturn(Timestamp.valueOf(LocalDateTime.now()));
        given(agendaMapper.modelToDto(agenda)).willReturn(agendaDto);
        given(agendaRepository.searchAvailabilityBy(any(), anyLong(), anyList(), any(Timestamp.class), any(Timestamp.class)))
          .willReturn(new PageImpl<>(Collections.singletonList(agenda)));

        Page<AgendaDto> agendaDtoPage =
          agendaServiceSpy.getAvailability(
            SearchInterviewsAvailabilityDto
              .builder()
              .candidateId(1L)
              .interviewerIds(Collections.emptyList())
              .startingFrom(OffsetDateTime.now())
              .endingAt(OffsetDateTime.now().plusDays(5))
              .build());

        assertThat(agendaDtoPage).isNotNull();
        assertThat(agendaDtoPage).isNotEmpty();
        assertThat(agendaDtoPage).hasSize(1);

        then(agendaMapper).should(times(1)).modelToDto(any(Agenda.class));
        then(agendaRepository).should(times(1)).searchAvailabilityBy(any(Pageable.class), anyLong(), anyList(), any(Timestamp.class), any(Timestamp.class));
        then(agendaServiceSpy).should(times(1)).validatesUserInputWithUserType(anyLong(), any(UserType.class));
        then(agendaServiceSpy).should(times(1)).validatesUsersInputWithUserType(anyList(), any(UserType.class));
        then(agendaServiceSpy).should(times(1)).validatesPeriodInput(any(OffsetDateTime.class), any(OffsetDateTime.class));
        then(agendaServiceSpy).should(times(1)).getAvailability(any(SearchInterviewsAvailabilityDto.class));
        then(dateMapper).should(times(2)).asTimestamp(any(OffsetDateTime.class));
        then(dateMapper).shouldHaveNoMoreInteractions();
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
        then(agendaRepository).shouldHaveNoMoreInteractions();
        then(agendaMapper).shouldHaveNoMoreInteractions();
    }


    @Test
    void createAvailability() {

    }

    @Test
    void validateAvailabilities() {
    }

    @Test
    void validateAvailability() {
    }

    @Test
    void validatesUserInput() {
    }

    @Test
    void validatesPeriodInput() {
    }

    @Test
    void validatesUsersInputWithUserType() {
    }

    @Test
    void validatesUserInputWithUserType() {
    }
}