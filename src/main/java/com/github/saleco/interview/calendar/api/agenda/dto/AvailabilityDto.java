package com.github.saleco.interview.calendar.api.agenda.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Availability Period")
public class AvailabilityDto {

    @Schema(description = "Start from", required = true, format = "date-time", example = "2021-03-25T09:00:00Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDateTime start;

    @Schema(description = "Ending at", required = true, format = "date-time", example = "2021-03-25T14:00:00Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDateTime end;

}
