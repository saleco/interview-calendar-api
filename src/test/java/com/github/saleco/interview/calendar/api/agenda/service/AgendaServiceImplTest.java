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
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
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

        AgendaDto agendaDto = AgendaDto.builder().userId(1L).build();

        Assertions.assertThrows(IllegalArgumentException.class,
          () -> agendaServiceSpy.createAgenda(agendaDto));

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

        AgendaDto agendaDto = AgendaDto.builder().userId(1L).build();
        Assertions.assertThrows(NotFoundException.class,
          () -> agendaServiceSpy.createAgenda(agendaDto));

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

        List<AgendaDto> agendaDtos = Collections.singletonList(AgendaDto.builder().userId(1L).build());

        Assertions.assertThrows(IllegalArgumentException.class,
          () -> agendaServiceSpy.createAgendas(agendaDtos));

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

        List<AgendaDto> agendaDtos = Collections.singletonList(AgendaDto.builder().userId(1L).build());

        Assertions.assertThrows(NotFoundException.class,
          () -> agendaServiceSpy.createAgendas(agendaDtos));

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

        List<AgendaDto> agendaDtos = Collections.singletonList(agendaDto);

        Assertions.assertThrows(ValidationException.class,
          () -> agendaServiceSpy.createAgendas(agendaDtos));

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

        Assertions.assertAll(
          () -> assertThat(agendaDtosReturned).isNotNull(),
          () -> assertThat(agendaDtosReturned).isNotEmpty(),
          () -> assertThat(agendaDtosReturned.get(0).getUserId()).isEqualTo(1L)
        );


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

        SearchInterviewsAvailabilityDto searchInterviewsAvailabilityDto
          = SearchInterviewsAvailabilityDto.builder().candidateId(1L).build();

        Assertions.assertThrows(IllegalArgumentException.class,
          () -> agendaServiceSpy.getAvailability(searchInterviewsAvailabilityDto));

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

        SearchInterviewsAvailabilityDto searchInterviewsAvailabilityDto = SearchInterviewsAvailabilityDto.builder().candidateId(1L).build();

        Assertions.assertThrows(NotFoundException.class,
          () -> agendaServiceSpy.getAvailability(searchInterviewsAvailabilityDto));

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

        SearchInterviewsAvailabilityDto searchInterviewsAvailabilityDto =
          SearchInterviewsAvailabilityDto.builder().candidateId(1L).interviewerIds(Collections.emptyList()).build();

        Assertions.assertThrows(IllegalArgumentException.class,
          () -> agendaServiceSpy.getAvailability(searchInterviewsAvailabilityDto));

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

        SearchInterviewsAvailabilityDto searchInterviewsAvailabilityDto =
          SearchInterviewsAvailabilityDto
            .builder()
            .candidateId(1L)
            .interviewerIds(Collections.emptyList())
            .startingFrom(OffsetDateTime.now())
            .endingAt(OffsetDateTime.now().plusDays(5))
            .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> agendaServiceSpy.getAvailability(searchInterviewsAvailabilityDto));

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

        Assertions.assertAll(
          () -> assertThat(agendaDtoPage).isNotNull(),
          () -> assertThat(agendaDtoPage).isNotEmpty(),
          () -> assertThat(agendaDtoPage).hasSize(1)
        );

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


    @DisplayName("Given Create Agenda DTO When createAvailability then validateUserInput throws IllegalArgumentsException")
    @Test
    void givenCreateAgendaDtoWhenCreateAvailabilityValidateUserInputThenShouldThrowIllegalArgumentsException() {
        doThrow(IllegalArgumentException.class).when(agendaServiceSpy).validatesUserInput(1L);

        CreateAgendaDto createAgendaDto = CreateAgendaDto.builder().userId(1L).build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> agendaServiceSpy.createAvailability(createAgendaDto));

        then(agendaRepository).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInput(anyLong());
        then(agendaServiceSpy).should(times(1)).createAvailability(any(CreateAgendaDto.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Create Agenda DTO When createAvailability then validateUserInput throws NotFoundException")
    @Test
    void givenCreateAgendaDtoWhenCreateAvailabilityValidateUserInputThenShouldThrowINotFoundException() {
        doThrow(NotFoundException.class).when(agendaServiceSpy).validatesUserInput(1L);

        CreateAgendaDto createAgendaDto = CreateAgendaDto.builder().userId(1L).build();

        Assertions.assertThrows(NotFoundException.class, () -> agendaServiceSpy.createAvailability(createAgendaDto));

        then(agendaRepository).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInput(anyLong());
        then(agendaServiceSpy).should(times(1)).createAvailability(any(CreateAgendaDto.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Create Agenda DTO When createAvailability then validateAvailabilities throws IllegalArgumentException")
    @Test
    void givenCreateAgendaDtoWhenCreateAvailabilityValidateAvailabilitiesThenShouldThrowIllegalArgumentException() {
        CreateAgendaDto createAgendaDto =
          CreateAgendaDto
            .builder()
            .userId(1L)
            .availabilities(Collections.singletonList(AvailabilityDto.builder().build()))
            .build();

        doNothing().when(agendaServiceSpy).validatesUserInput(createAgendaDto.getUserId());

        doThrow(IllegalArgumentException.class)
          .when(agendaServiceSpy).validateAvailabilities(createAgendaDto.getAvailabilities());

        Assertions.assertThrows(IllegalArgumentException.class,
          () -> agendaServiceSpy.createAvailability(createAgendaDto));

        then(agendaRepository).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInput(anyLong());
        then(agendaServiceSpy).should(times(1)).validateAvailabilities(anyList());
        then(agendaServiceSpy).should(times(1)).createAvailability(any(CreateAgendaDto.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Create Agenda DTO When createAvailability then validateAvailabilities throws ValidationException")
    @Test
    void givenCreateAgendaDtoWhenCreateAvailabilityValidateAvailabilitiesThenShouldValidationException() {
        CreateAgendaDto createAgendaDto =
          CreateAgendaDto
            .builder()
            .userId(1L)
            .availabilities(Collections.singletonList(AvailabilityDto.builder().build()))
            .build();

        doNothing().when(agendaServiceSpy).validatesUserInput(createAgendaDto.getUserId());

        doThrow(ValidationException.class)
          .when(agendaServiceSpy).validateAvailabilities(createAgendaDto.getAvailabilities());

        Assertions.assertThrows(ValidationException.class,
          () -> agendaServiceSpy.createAvailability(createAgendaDto));

        then(agendaRepository).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInput(anyLong());
        then(agendaServiceSpy).should(times(1)).validateAvailabilities(anyList());
        then(agendaServiceSpy).should(times(1)).createAvailability(any(CreateAgendaDto.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Create Agenda DTO When createAvailability then Should return List of AgendaDto")
    @Test
    void givenCreateAgendaDtoWhenCreateAvailabilityThenShouldReturnAgendaDtoList() {
        CreateAgendaDto createAgendaDto =
          CreateAgendaDto
            .builder()
            .userId(1L)
            .availabilities(Collections.singletonList(AvailabilityDto.builder().build()))
            .build();

        List<AgendaDto> agendaDtos = Collections.singletonList(AgendaDto.builder().build());

        doNothing().when(agendaServiceSpy).validatesUserInput(createAgendaDto.getUserId());
        doNothing().when(agendaServiceSpy).validateAvailabilities(createAgendaDto.getAvailabilities());

        doReturn(agendaDtos)
          .when(agendaServiceSpy).getAgendaDtosFromAvailabilities(createAgendaDto.getAvailabilities(), createAgendaDto.getUserId());

        doReturn(agendaDtos).when(agendaServiceSpy).createAgendas(agendaDtos);

        List<AgendaDto> agendaDtosReturned = agendaServiceSpy.createAvailability(createAgendaDto);

        Assertions.assertAll(
          () -> assertThat(agendaDtosReturned).isNotNull(),
          () -> assertThat(agendaDtosReturned).isNotEmpty()
        );

        then(agendaRepository).shouldHaveNoInteractions();
        then(agendaMapper).shouldHaveNoInteractions();
        then(agendaServiceSpy).should(times(1)).validatesUserInput(anyLong());
        then(agendaServiceSpy).should(times(1)).validateAvailabilities(anyList());
        then(agendaServiceSpy).should(times(1)).getAgendaDtosFromAvailabilities(anyList(), anyLong());
        then(agendaServiceSpy).should(times(1)).createAgendas(anyList());
        then(agendaServiceSpy).should(times(1)).createAvailability(any(CreateAgendaDto.class));
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }


    @DisplayName("Given Existing AgendaDto When Validate Agenda then should throw ValidationException")
    @Test
    void givenExistingAgendaDtoWhenValidateAgendaThenShouldThrowValidationException() {
        Agenda agenda = Agenda.builder().build();
        AgendaDto agendaDto = AgendaDto.builder().build();

        doReturn(agenda).when(agendaMapper).dtoToModel(agendaDto);

        doReturn(true).when(agendaRepository).exists(any(Example.class));

        Assertions.assertThrows(ValidationException.class, () -> agendaService.validateAgenda(agendaDto));

        then(agendaRepository).should(times(1)).exists(any(Example.class));
        then(agendaMapper).should(times(1)).dtoToModel(any(AgendaDto.class));
        then(agendaRepository).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given AgendaDto When Validate Agenda Validate Availability then should throw IllegalArgumentException")
    @Test
    void givenAgendaDtoWhenValidateAgendaValidateAvailabilityThenShouldThrowIllegalArgumentException() {
        Agenda agenda = Agenda.builder().build();
        AgendaDto agendaDto = AgendaDto.builder()
          .start(OffsetDateTime.now())
          .end(OffsetDateTime.now().plusDays(5))
          .build();

        doReturn(agenda).when(agendaMapper).dtoToModel(agendaDto);
        doReturn(false).when(agendaRepository).exists(any(Example.class));
        doThrow(IllegalArgumentException.class).when(agendaServiceSpy).validateAvailability(any(OffsetDateTime.class), any(OffsetDateTime.class));


        Assertions.assertThrows(IllegalArgumentException.class, () -> agendaServiceSpy.validateAgenda(agendaDto));

        then(agendaServiceSpy).should(times(1)).validateAvailability(any(OffsetDateTime.class), any(OffsetDateTime.class));
        then(agendaServiceSpy).should(times(1)).validateAgenda(any(AgendaDto.class));
        then(agendaRepository).should(times(1)).exists(any(Example.class));
        then(agendaMapper).should(times(1)).dtoToModel(any(AgendaDto.class));
        then(agendaRepository).shouldHaveNoMoreInteractions();
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given AgendaDto When Validate Agenda Validate Availability then should throw ValidationException")
    @Test
    void givenAgendaDtoWhenValidateAgendaValidateAvailabilityThenShouldThrowValidationException() {
        Agenda agenda = Agenda.builder().build();
        AgendaDto agendaDto = AgendaDto.builder()
          .start(OffsetDateTime.now())
          .end(OffsetDateTime.now().plusDays(5))
          .build();

        doReturn(agenda).when(agendaMapper).dtoToModel(agendaDto);
        doReturn(false).when(agendaRepository).exists(any(Example.class));
        doThrow(ValidationException.class).when(agendaServiceSpy).validateAvailability(any(OffsetDateTime.class), any(OffsetDateTime.class));


        Assertions.assertThrows(ValidationException.class, () -> agendaServiceSpy.validateAgenda(agendaDto));

        then(agendaServiceSpy).should(times(1)).validateAvailability(any(OffsetDateTime.class), any(OffsetDateTime.class));
        then(agendaServiceSpy).should(times(1)).validateAgenda(any(AgendaDto.class));
        then(agendaRepository).should(times(1)).exists(any(Example.class));
        then(agendaMapper).should(times(1)).dtoToModel(any(AgendaDto.class));
        then(agendaRepository).shouldHaveNoMoreInteractions();
        then(agendaServiceSpy).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Empty List of AvailabilityDto and userId When getAgendaDtosFromAvailabilities then should return Empty List")
    @Test
    void givenEmptyAvailabilityDtoListAndUserIdWhenGetAgendaDtosFromAvailabilitiesThenShouldReturnEmptyList() {
        assertThat(agendaService.getAgendaDtosFromAvailabilities(Lists.emptyList(), null)).isEmpty();
    }

    @DisplayName("Given List of AvailabilityDto and userId When getAgendaDtosFromAvailabilities then should return AgendaDto List")
    @Test
    void givenAvailabilityDtoListAndUserIdWhenGetAgendaDtosFromAvailabilitiesThenShouldReturnAgendaDtoList() {

        AvailabilityDto availabilityDto =
          AvailabilityDto.builder().start(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS)).end(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(4)).build();

        List<AgendaDto> agendaDtos = agendaService.getAgendaDtosFromAvailabilities(Collections.singletonList(availabilityDto), null);

        Assertions.assertAll(
          () -> assertThat(agendaDtos).isNotNull(),
          () -> assertThat(agendaDtos).isNotEmpty(),
          () -> assertThat(agendaDtos).hasSize(4)
        );
    }


    @DisplayName("Given Null Start or End When validateAvailability then should throw IllegalArgumentException")
    @Test
    void givenNullStartOrEndWhenValidateAvailabilityThenShouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> agendaService.validateAvailability(null, null));
    }

    @DisplayName("Given Invalid Minute Start or End When validateAvailability then should throw ValidationException")
    @Test
    void givenInvalidStartOrEndWhenValidateAvailabilityThenShouldThrowValidationException() {
        Assertions.assertThrows(ValidationException.class, () ->
          agendaService.validateAvailability(
            OffsetDateTime.of(2021, 2, 2, 5, 15, 0, 0, ZoneOffset.UTC),
            OffsetDateTime.now()));
    }

    @DisplayName("Given Start after End When validateAvailability then should throw ValidationException")
    @Test
    void givenStartAfterEndWhenValidateAvailabilityThenShouldThrowValidationException() {
        Assertions.assertThrows(ValidationException.class, () ->
          agendaService.validateAvailability(
            OffsetDateTime.of(2021, 2, 2, 5, 0, 0, 0, ZoneOffset.UTC),
            OffsetDateTime.of(2021, 2, 2, 4, 0, 0, 0, ZoneOffset.UTC)));
    }

    @DisplayName("Given Null Start or End When validatesPeriodInput then should throw IllegalArgumentException")
    @Test
    void givenNullStartOrEndWhenValidatesPeriodInputThenShouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> agendaService.validatesPeriodInput(null, null));
    }

    @DisplayName("Given days between Start and End greater then 5 When validatesPeriodInput then should throw ValidationException")
    @Test
    void givenDaysBetweenStartAndEndGreaterThenFiveWhenValidatesPeriodInputThenShouldThrowValidationException() {
        Assertions.assertThrows(ValidationException.class, () -> agendaService.validatesPeriodInput(
          OffsetDateTime.of(2021, 2, 2, 5, 0, 0, 0, ZoneOffset.UTC),
          OffsetDateTime.of(2021, 2, 10, 4, 0, 0, 0, ZoneOffset.UTC)));
    }

}