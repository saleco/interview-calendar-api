package com.github.saleco.interview.calendar.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Pagination Dto")
public class PageableDto {

    @Schema(description = "Page Number. Starting with 0", example = "0", defaultValue = "0")
    private int pageNumber;

    @Schema(description = "Page size", example = "20", defaultValue = "20")
    private int pageSize = 20;

}
