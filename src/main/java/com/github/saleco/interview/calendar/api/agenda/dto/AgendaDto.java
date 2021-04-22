package com.github.saleco.interview.calendar.api.agenda.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.saleco.interview.calendar.api.user.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Agenda")
public class AgendaDto {

    @Schema(description = "Agenda's Identification", required = true, example = "1")
    private Long id;

    @Schema(description = "Agenda's User", required = true)
    private Long userId;

    @Schema(description = "Agenda's Start Time", required = true, example = "2020-01-01 10:00")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
    private OffsetDateTime start;

    @Schema(description = "Agenda's End Time", required = true, example = "2020-01-01 11:00")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
    private OffsetDateTime end;

}
