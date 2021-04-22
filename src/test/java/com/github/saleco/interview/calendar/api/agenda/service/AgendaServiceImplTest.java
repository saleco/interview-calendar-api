package com.github.saleco.interview.calendar.api.agenda.service;

import com.github.saleco.interview.calendar.api.agenda.dto.AgendaDto;
import com.github.saleco.interview.calendar.api.agenda.mapper.AgendaMapper;
import com.github.saleco.interview.calendar.api.agenda.model.Agenda;
import com.github.saleco.interview.calendar.api.agenda.repository.AgendaRepository;
import com.github.saleco.interview.calendar.api.mapper.DateMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

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

//    @DisplayName("Given AgendaDto When createAgenda then should return AgendaDto")
//    @Test
//    void givenAgendaDtoWhenCreateAgendaThenShouldReturnAgendaDto() {
//        Agenda agenda = mock(Agenda.class);
//        AgendaDto agendaDto = mock(AgendaDto.class);
//        given(agendaMapper.modelToDto(agenda)).willReturn(agendaDto);
//        given(agendaMapper.dtoToModel(agendaDto)).willReturn(agenda);
//        given(agendaRepository.save(agenda)).willReturn(agenda);
//
//        AgendaDto agendaDtoReturned = agendaService.createAgenda(agendaDto);
//
//        assertThat(agendaDtoReturned).isNotNull();
//
//        then(agendaRepository).should(times(1)).save(agenda);
//        then(agendaMapper).should(times(1)).modelToDto(agenda);
//        then(agendaMapper).should(times(1)).dtoToModel(agendaDto);
//        then(agendaRepository).shouldHaveNoMoreInteractions();
//        then(agendaMapper).shouldHaveNoMoreInteractions();
//
//    }



}