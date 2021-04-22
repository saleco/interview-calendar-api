package com.github.saleco.interview.calendar.api.agenda.mapper;

import com.github.saleco.interview.calendar.api.agenda.model.Agenda;
import com.github.saleco.interview.calendar.api.agenda.dto.AgendaDto;
import com.github.saleco.interview.calendar.api.mapper.DateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {DateMapper.class})
public interface AgendaMapper {


    @Mapping(source = "user.id", target = "userId")
    AgendaDto modelToDto(Agenda agenda);

    @Mapping(source = "userId", target = "user.id")
    Agenda dtoToModel(AgendaDto agendaDto);

}
