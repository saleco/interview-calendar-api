package com.github.saleco.interview.calendar.api.agenda.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Create Agenda")
public class CreateAgendaDto {

    @Schema(description = "Agendas", required = true)
    @NotEmpty
    private List<AvailabilityDto> availabilities;

    @Schema(description = "Agenda's User", required = true, example = "1")
    @NotNull
    private Long userId;

}
